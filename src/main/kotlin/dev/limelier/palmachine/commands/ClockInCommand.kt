package dev.limelier.palmachine.commands

import dev.limelier.palmachine.di
import dev.limelier.palmachine.service.SessionService
import dev.minn.jda.ktx.messages.reply_
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent
import org.kodein.di.instance

fun setupClockInCommand() {
    registerSlashCommand(
        "clockin",
        "Start a work session",
        ::handle
    )
}

private fun handle(event: GenericCommandInteractionEvent) {
    val sessionService: SessionService by di.instance()
    val result = sessionService.start(event.user)

    if (result != null) {
        event.reply_("Clocked in.").queue()
    } else {
        event.reply_("You are already clocked in!", ephemeral = true).queue()
    }
}