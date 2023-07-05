package dev.limelier.palmachine.model

import net.dv8tion.jda.api.entities.User
import java.time.Instant

data class Session(
    val user: User,
    val start: Instant = Instant.now(),
    val end: Instant? = null,
) {
    val open get() = end == null

    fun closed() = Session(user, start, Instant.now())
}
