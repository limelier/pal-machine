package dev.limelier.palmachine.commands

import dev.limelier.palmachine.Configuration
import dev.limelier.palmachine.di
import dev.limelier.palmachine.service.SessionService
import dev.limelier.palmachine.util.humanHMS
import dev.minn.jda.ktx.messages.reply_
import net.dv8tion.jda.api.entities.Guild
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
    val guild: Guild by di.instance()
    val config: Configuration by di.instance()

    val session = sessionService.end(event.user)
        ?: return event.reply_("You are not clocked in!", ephemeral = true).queue()

    val duration = session.duration
    event.reply_("Clocked out after ${duration.humanHMS()}.").queue()
    config.guild.activeRoleId?.also {
        val role = guild.getRoleById(it)!!
        guild.removeRoleFromMember(event.user, role).queue()
    }
}