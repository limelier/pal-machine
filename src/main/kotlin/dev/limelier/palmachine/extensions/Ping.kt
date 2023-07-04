package dev.limelier.palmachine.extensions

import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.ephemeralSlashCommand
import com.kotlindiscord.kord.extensions.types.respond

class PingExtension : Extension() {
    override val name: String = "ping"

    override suspend fun setup() {
        ephemeralSlashCommand {
            name = "ping"
            description = "Check if the bot is alive."

            action {
                respond { content = "pong" }
            }
        }
    }

}