package dev.limelier.palmachine

/** The state of the bot does not match expected values. */
class BotStateException(override val message: String?) : Exception(message)