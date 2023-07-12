package dev.limelier.palmachine.model

import net.dv8tion.jda.api.entities.User
import java.time.Duration
import java.time.Instant

data class Session(
    val id: Int,
    val user: User,
    val start: Instant = Instant.now(),
    val end: Instant? = null,
) {
    val open get() = end == null
    val duration: Duration get() = Duration.between(start, end ?: Instant.now())

    fun closed() = Session(id, user, start, Instant.now())
}
