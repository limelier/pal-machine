package dev.limelier.palmachine.commands

import dev.limelier.palmachine.di
import dev.limelier.palmachine.service.SessionService
import dev.limelier.palmachine.util.humanHMS
import dev.minn.jda.ktx.messages.reply_
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent
import org.kodein.di.instance

fun setupUserTotalCommand() {
    registerSlashCommand(
        "total",
        "Display your total amount of time worked",
        ::handle
    )
}

private fun handle(event: GenericCommandInteractionEvent) {
    val sessionService: SessionService by di.instance()
    val totalDuration = sessionService.total(event.user)

    event.reply_(
        content = if (totalDuration != null) {
            "So far, you've grinded for ${totalDuration.humanHMS()}."
        } else {
            "You haven't clocked in any sessions."
        },
        ephemeral = true
    ).queue()
}