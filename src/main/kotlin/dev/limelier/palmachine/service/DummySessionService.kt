package dev.limelier.palmachine.service

import dev.limelier.palmachine.model.Session
import net.dv8tion.jda.api.entities.User

/**
 * Placeholder in-memory session "database" service.
 */
class DummySessionService : SessionService {
    private val sessions = mutableListOf<Session>()

    override fun start(user: User): Session? {
        if (findOpenSession(user) != null) return null

        val session = Session(user)
        sessions.add(session)
        return session
    }

    override fun end(user: User): Session? {
        val openSession = findOpenSession(user) ?: return null
        sessions.remove(openSession)
        val closedSession = openSession.closed()
        sessions.add(closedSession)
        return closedSession
    }

    private fun findOpenSession(user: User): Session? = sessions.find { it.user == user && it.open }
}