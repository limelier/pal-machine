package dev.limelier.palmachine.commands

import dev.limelier.palmachine.di
import dev.minn.jda.ktx.events.onCommand
import dev.minn.jda.ktx.interactions.commands.upsertCommand
import io.github.oshai.kotlinlogging.KotlinLogging
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData
import org.kodein.di.instance

private val logger = KotlinLogging.logger {}

private val jda: JDA by di.instance()
private val guild: Guild by di.instance()

/**
 * Register a slash command on the guild.
 *
 * @param name the name by which the command will be invoked
 * @param description the help text shown for the command
 * @param data additional data for the slash command, like arguments
 * @param handler a function that takes a command interaction event and handles it
 */
fun registerSlashCommand(
    name: String,
    description: String,
    handler: (GenericCommandInteractionEvent) -> Unit,
    data: (SlashCommandData.() -> Unit)? = null
) {
    if (data != null) {
        guild.upsertCommand(name, description, data).queue()
    } else {
        guild.upsertCommand(name, description).queue()
    }
    logger.info { "Registered command $name" }

    jda.onCommand(name) {
        logger.info { "Received call on /$name from ${it.user.name}" }
        handler(it)
    }
}