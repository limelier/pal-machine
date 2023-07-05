package dev.limelier.palmachine.service

import dev.limelier.palmachine.model.Session
import net.dv8tion.jda.api.entities.User
import java.time.Duration

class DummySessionService {
    private val sessions = mutableListOf<Session>()

    fun clockIn(user: User): Boolean {
        if (findOpenSession(user) != null) return false

        val session = Session(user)
        sessions.add(session)
        return true
    }

    fun clockOut(user: User): Duration? {
        val openSession = findOpenSession(user) ?: return null
        sessions.remove(openSession)
        val closedSession = openSession.closed()
        sessions.add(closedSession)
        return Duration.between(closedSession.start, closedSession.end)
    }

    private fun findOpenSession(user: User): Session? {
        return sessions.find { it.user == user && it.open }
    }
}