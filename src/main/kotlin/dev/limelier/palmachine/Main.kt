package dev.limelier.palmachine

import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addFileSource
import dev.limelier.palmachine.commands.*
import dev.limelier.palmachine.service.SessionService
import dev.minn.jda.ktx.jdabuilder.light
import io.github.oshai.kotlinlogging.KotlinLogging
import org.jooq.impl.DSL
import org.kodein.di.DI
import org.kodein.di.bindSingleton

private val logger = KotlinLogging.logger {}

private val config = ConfigLoaderBuilder.default()
            .addFileSource(System.getenv("BOT_CONFIG") ?: "./config.yml")
            .build()
            .loadConfigOrThrow<Configuration>()
private val jda = light(System.getenv("BOT_TOKEN"))
private val guild = jda.awaitReady().let {
    if (config.guild.id != null) {
        it.getGuildById(config.guild.id)
            ?: throw BotStateException("a guild id (${config.guild.id}) was present, but the bot was not connected to it")
    } else {
        val g = it.guilds[0]
        logger.info { "Guild ID not configured, selected first connected guild (${g.name})" }
        return@let g
    }
}

val di = DI {
    bindSingleton { jda }
    bindSingleton { SessionService() }
    bindSingleton { guild }
    bindSingleton { config }
    bindSingleton { DSL.using(config.db.connectionString, config.db.user, config.db.password.value) }
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