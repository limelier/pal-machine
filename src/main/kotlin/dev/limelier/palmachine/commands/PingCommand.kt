package dev.limelier.palmachine.commands

import dev.minn.jda.ktx.messages.reply_
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent

fun setupPingCommand() {
    registerSlashCommand(
        "ping",
        "Check if the bot is alive",
        ::handle
    )
}

private fun handle(event: GenericCommandInteractionEvent) {
    event.reply_("Pong!", ephemeral = true).queue()
}