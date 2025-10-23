package com.teacher.teacherhelper.bot

import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.Bot
import org.springframework.beans.factory.DisposableBean
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component


@Component
class BotRunner(
    private val bot: Bot,
) : ApplicationRunner, DisposableBean {

    override fun run(args: ApplicationArguments) = runBlocking {
        bot.login()
    }

    override fun destroy() = bot.close()
}
