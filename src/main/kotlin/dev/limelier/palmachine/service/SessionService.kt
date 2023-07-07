package dev.limelier.palmachine.service

import dev.limelier.palmachine.model.Session
import net.dv8tion.jda.api.entities.User

interface SessionService {
    /**
     * Start a session for `user` if one doesn't already exist, and return it.
     */
    fun start(user: User): Session?

    /**
     * End the `user`'s current session if one exists, and return it.
     */
    fun end(user: User): Session?
}
