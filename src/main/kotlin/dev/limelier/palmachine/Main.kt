package dev.limelier.palmachine

import dev.limelier.palmachine.service.DummySessionService
import dev.minn.jda.ktx.coroutines.await
import dev.minn.jda.ktx.events.onCommand
import dev.minn.jda.ktx.interactions.commands.slash
import dev.minn.jda.ktx.interactions.commands.updateCommands
import dev.minn.jda.ktx.jdabuilder.light
import dev.minn.jda.ktx.messages.reply_

suspend fun main() {
    val token = System.getenv("BOT_TOKEN")
    val jda = light(token).awaitReady()
    val guild = jda.guilds.first()
    val sessionService = DummySessionService()

    guild.updateCommands {
        slash("ping", "Check if the bot is alive")
        slash("clockin", "Start a session of work")
        slash("clockout", "End a session of work")
    }.await()

    jda.onCommand("ping") { event ->
        event.reply_("Pong!", ephemeral = true).queue()
    }

    jda.onCommand("clockin") { event ->
        val result = sessionService.clockIn(event.user)

        if (result) {
            event.reply_("Clocked in.").queue()
        } else {
            event.reply_("You are already clocked in!", ephemeral = true).queue()
        }
    }

    jda.onCommand("clockout") { event ->
        val duration = sessionService.clockOut(event.user)

        if (duration != null) {
            event.reply_("Clocked out after ${duration.toHours()}h${duration.toMinutesPart()}m${duration.toSecondsPart()}s.").queue()
        } else {
            event.reply_("You are not clocked in!", ephemeral = true).queue()
        }
    }
}