package dev.limelier.palmachine.service

import dev.limelier.palmachine.Configuration
import dev.limelier.palmachine.di
import dev.limelier.palmachine.model.Session
import dev.limelier.tables.references.SESSIONS
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.User
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.impl.DSL.noCondition
import org.kodein.di.instance
import java.time.Duration
import java.time.Instant
import java.time.ZoneOffset

class JooqSessionService : SessionService {
    private val ctx: DSLContext by di.instance()
    private val jda: JDA by di.instance()
    private val config: Configuration by di.instance()
    private val guild: Guild by di.instance()

    private val enrolledRole = config.guild.enrolledRoleId?.let { jda.getRoleById(it) }

    override fun start(user: User): Session? {
        if (getOpenSession(user) != null) {
            return null
        }

        return ctx.insertInto(SESSIONS)
            .set(SESSIONS.USER_ID, user.idLong)
            .set(SESSIONS.START, Instant.now().atOffset(ZoneOffset.UTC))
            .returningResult()
            .fetchOne { it.asSession() }
    }

    override fun end(user: User): Session? {
        val openSession = getOpenSession(user) ?: return null
        val closedSession = openSession.closed()
        val record = ctx.newRecord(SESSIONS, closedSession)
        record.update()
        return closedSession
    }

    override fun forUser(user: User?, limit: Int?): List<Session> {
        val sessions = ctx.select()
            .from(SESSIONS)
            .where(if (user != null) SESSIONS.USER_ID.eq(user.idLong) else noCondition())
            .orderBy(SESSIONS.START.desc())
            .limit(limit)
            .fetch { it.asSession() }

        return sessions.reversed()
    }

    override fun userTotalDuration(user: User): Duration? {
        return ctx.select()
            .from(SESSIONS)
            .where(SESSIONS.USER_ID.eq(user.idLong))
            .orderBy(SESSIONS.START.desc())
            .fetch { it.asSession() }
            .map { it.duration }
            .reduceOrNull { acc, duration -> acc + duration }
    }

    override fun userTotalDurations(): Map<User, Duration> =
        allUsers().associateWith { userTotalDuration(it) ?: Duration.ZERO }

    private fun getOpenSession(user: User): Session? {
        return ctx.select()
            .from(SESSIONS)
            .where(SESSIONS.USER_ID.eq(user.idLong))
            .and(SESSIONS.END.isNull)
            .fetchOne { it.asSession() }
    }

    private fun allUsers(): List<User> {
        return if (enrolledRole != null) {
            guild.findMembersWithRoles(enrolledRole).get().map { it.user }
        } else {
            ctx.selectDistinct(SESSIONS.USER_ID)
                .from(SESSIONS)
                .fetch { jda.retrieveUserById(it[SESSIONS.USER_ID]!!).complete() }
        }
    }

    private fun Record.asSession(): Session {
        return Session(
            this[SESSIONS.ID]!!,
            jda.retrieveUserById(this[SESSIONS.USER_ID]!!).complete(),
            this[SESSIONS.START]!!.toInstant(),
            this[SESSIONS.END]?.toInstant(),
        )
    }
}