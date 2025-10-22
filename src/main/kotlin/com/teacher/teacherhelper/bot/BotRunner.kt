package com.teacher.teacherhelper.bot

import com.teacher.teacherhelper.bot.api.BotSender
import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.Bot
import org.springframework.beans.factory.DisposableBean
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class BotRunner(
    private val bot: Bot,
    private val botSender:BotSender
) : ApplicationRunner, DisposableBean {

    override fun run(args: ApplicationArguments) = runBlocking {
        bot.login()
        botSender.sendGroupMsg(375775892,"豆子复活测试")
        Unit
    }

    override fun destroy() = bot.close()
}
