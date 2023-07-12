package dev.limelier.palmachine.commands

import dev.limelier.palmachine.di
import dev.limelier.palmachine.service.SessionService
import dev.limelier.palmachine.util.humanHMS
import dev.limelier.palmachine.util.timestamp
import dev.minn.jda.ktx.interactions.commands.option
import dev.minn.jda.ktx.interactions.components.getOption
import dev.minn.jda.ktx.messages.send
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent
import net.dv8tion.jda.api.utils.TimeFormat
import org.kodein.di.instance

private const val DEFAULT_LIMIT = 5
fun setupUserHistoryCommand() {
    registerSlashCommand(
        "history",
        "Display your last sessions",
        ::handle
    ) {
        option<Int>("limit", "How many sessions to limit the history to (default: $DEFAULT_LIMIT)")
    }
}

private fun handle(event: GenericCommandInteractionEvent) {
    event.deferReply(true).queue()

    val sessionService: SessionService by di.instance()
    val limit = event.getOption<Int>("limit") ?: DEFAULT_LIMIT

    val sessions = sessionService.forUser(event.user, limit)

    val message = if (sessions.isNotEmpty()) {
        buildString {
            appendLine("Your last $limit sessions:")
            for (session in sessions) {
                val start = session.start.timestamp(TimeFormat.DATE_TIME_LONG)
                appendLine("- $start ${if (session.open) "(on-going)" else ""} - ${session.duration.humanHMS()}")
            }
        }
    } else {
        "You haven't clocked in any sessions."
    }

    event.hook.send(content = message).queue()
}