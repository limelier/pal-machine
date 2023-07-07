package dev.limelier.palmachine.commands

import dev.limelier.palmachine.di
import dev.limelier.palmachine.service.SessionService
import dev.limelier.palmachine.util.humanHMS
import dev.minn.jda.ktx.messages.reply_
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent
import org.kodein.di.instance

fun setupClockOutCommand() {
    registerSlashCommand(
        "clockout",
        "End a work session",
        ::handle
    )
}

private fun handle(event: GenericCommandInteractionEvent) {
    val sessionService: SessionService by di.instance()
    val session = sessionService.end(event.user)

    if (session != null) {
        val duration = session.duration
        event.reply_(
            "Clocked out after ${duration.humanHMS()}."
        ).queue()
    } else {
        event.reply_("You are not clocked in!", ephemeral = true).queue()
    }
}