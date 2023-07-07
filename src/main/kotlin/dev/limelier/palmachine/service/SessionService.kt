package dev.limelier.palmachine.service

import dev.limelier.palmachine.model.Session
import net.dv8tion.jda.api.entities.User
import java.time.Duration

interface SessionService {
    /**
     * Start a session for `user` if one doesn't already exist, and return it.
     */
    fun start(user: User): Session?

    /**
     * End the `user`'s current session if one exists, and return it.
     */
    fun end(user: User): Session?

    /**
     * Get the last `limit` sessions of one or all users, in chronological order.
     */
    fun forUser(
        user: User? = null,
        limit: Int? = null,
    ): List<Session>

    /**
     * Get the total the duration of all a user's sessions, or null if they have had none.
     */
    fun userTotalDuration(user: User): Duration?

    /**
     * Get the total duration of each user's sessions.
     */
    fun userTotalDurations(): Map<User, Duration>
}
