package dev.limelier.palmachine.service

import dev.limelier.palmachine.model.Session
import net.dv8tion.jda.api.entities.User
import java.time.Duration

/**
 * Placeholder in-memory session "database" service.
 */
class DummySessionService : SessionService {
    private val users = mutableSetOf<User>()
    private val sessions = mutableListOf<Session>()

    override fun start(user: User): Session? {
        if (findOpenSession(user) != null) return null

        val session = Session(user)
        sessions.add(session)
        users.add(user)
        return session
    }

    override fun end(user: User): Session? {
        val openSession = findOpenSession(user) ?: return null
        sessions.remove(openSession)
        val closedSession = openSession.closed()
        sessions.add(closedSession)
        return closedSession
    }

    override fun forUser(user: User?, limit: Int?): List<Session> {
        val userSessions = if (user != null) {
            sessions.filter { it.user == user }
        } else {
            sessions
        }

        return if (limit != null)
            userSessions.takeLast(limit)
        else
            userSessions
    }

    override fun userTotalDuration(user: User): Duration? {
        return forUser(user)
            .map { it.duration }
            .reduceOrNull { acc, duration -> acc + duration }
    }

    override fun userTotalDurations(): Map<User, Duration> = users.associateWith { user -> userTotalDuration(user)!! }

    private fun findOpenSession(user: User): Session? = sessions.find { it.user == user && it.open }
}