package dev.limelier.palmachine.commands

import dev.limelier.palmachine.di
import dev.limelier.palmachine.service.SessionService
import dev.minn.jda.ktx.events.onCommand
import dev.minn.jda.ktx.messages.reply_
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent
import org.kodein.di.instance
import java.time.Duration

private const val COMMAND = "clockout"

fun setupClockOutCommand() {
    val jda: JDA by di.instance()
    jda.upsertCommand(COMMAND, "End a work session")
    jda.onCommand(COMMAND) { handle(it) }
}

private fun handle(event: GenericCommandInteractionEvent) {
    val sessionService: SessionService by di.instance()
    val session = sessionService.end(event.user)

    if (session != null) {
        val duration = Duration.between(session.start, session.end)
        event.reply_(
            "Clocked out after ${duration.toHours()}h${duration.toMinutesPart()}m${duration.toSecondsPart()}s."
        ).queue()
    } else {
        event.reply_("You are not clocked in!", ephemeral = true).queue()
    }
}