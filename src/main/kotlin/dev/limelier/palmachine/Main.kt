package dev.limelier.palmachine

import dev.limelier.palmachine.commands.setupClockInCommand
import dev.limelier.palmachine.commands.setupClockOutCommand
import dev.limelier.palmachine.commands.setupPingCommand
import dev.limelier.palmachine.service.DummySessionService
import dev.limelier.palmachine.service.SessionService
import dev.minn.jda.ktx.jdabuilder.light
import net.dv8tion.jda.api.JDA
import org.kodein.di.DI
import org.kodein.di.bindSingleton

val di = DI {
    bindSingleton<JDA> { light(System.getenv("BOT_TOKEN")!!) }
    bindSingleton<SessionService> { DummySessionService() }
}
fun main() {
    setupPingCommand()
    setupClockInCommand()
    setupClockOutCommand()
}