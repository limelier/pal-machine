package dev.limelier.palmachine.commands

import dev.limelier.palmachine.di
import dev.minn.jda.ktx.events.onCommand
import dev.minn.jda.ktx.messages.reply_
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent
import org.kodein.di.instance

private const val COMMMAND = "ping"

fun setupPingCommand() {
    val jda: JDA by di.instance()
    jda.upsertCommand(COMMMAND, "Check if the bot is alive")
    jda.onCommand(COMMMAND) { handle(it) }
}

private fun handle(event: GenericCommandInteractionEvent) {
    event.reply_("Pong!", ephemeral = true).queue()
}