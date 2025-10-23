package com.teacher.teacherhelper.bot

import com.teacher.teacherhelper.bot.api.BotSender
import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.Bot
import org.springframework.ai.chat.client.ChatClient
import org.springframework.beans.factory.DisposableBean
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

const val DEFAULT_PROMPT = "你的名字叫豆子，是动漫《鬼灭之刃》里主角的妹妹弥豆子，请结合动漫里的设定介绍一下自己"

@Component
class BotRunner(
    private val bot: Bot,
    private val botSender:BotSender,
    private val chatClient: ChatClient,
) : ApplicationRunner, DisposableBean {

    override fun run(args: ApplicationArguments) = runBlocking {
        bot.login()
        val content = chatClient.prompt(DEFAULT_PROMPT).call().content() ?: "豆子ai调用失败"
        botSender.sendGroupMsg(375775892,content)

        Unit
    }

    override fun destroy() = bot.close()
}
