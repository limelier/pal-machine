package dev.limelier.palmachine

import dev.limelier.palmachine.commands.*
import dev.limelier.palmachine.service.DummySessionService
import dev.limelier.palmachine.service.SessionService
import dev.minn.jda.ktx.jdabuilder.light
import io.github.oshai.kotlinlogging.KotlinLogging
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Guild
import org.kodein.di.DI
import org.kodein.di.bindSingleton

private val logger = KotlinLogging.logger {}

private val jda = light(System.getenv("BOT_TOKEN"))
private val guild = jda.awaitReady().guilds[0]

val di = DI {
    bindSingleton<JDA> { jda }
    bindSingleton<SessionService> { DummySessionService() }
    bindSingleton<Guild> { guild }
}
fun main() {
    logger.info { "Connected to guild '${guild.name}'" }
    setupPingCommand()
    setupClockInCommand()
    setupClockOutCommand()
    setupUserTotalCommand()
    setupUserHistoryCommand()
    logger.info { "Set up all commands; bot ready" }
}