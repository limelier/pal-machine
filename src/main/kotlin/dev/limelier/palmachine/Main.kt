package dev.limelier.palmachine

import com.kotlindiscord.kord.extensions.ExtensibleBot
import com.kotlindiscord.kord.extensions.utils.env
import dev.limelier.palmachine.extensions.PingExtension

suspend fun main() {
    val bot = ExtensibleBot(env("BOT_TOKEN")) {
        extensions {
            add(::PingExtension)
        }
    }

    bot.start()
}