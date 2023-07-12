package dev.limelier.palmachine.commands

import dev.limelier.palmachine.Configuration
import dev.limelier.palmachine.di
import dev.limelier.palmachine.service.SessionService
import dev.minn.jda.ktx.messages.reply_
import net.dv8tion.jda.api.entities.Guild
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
    val guild: Guild by di.instance()
    val config: Configuration by di.instance()

    sessionService.start(event.user)
        ?: return event.reply_("You are already clocked in!", ephemeral = true).queue()

    event.reply_("Clocked in.").queue()
    config.guild.activeRoleId?.let {
        guild.addRoleToMember(event.user, guild.getRoleById(it)!!).queue()
    }
}