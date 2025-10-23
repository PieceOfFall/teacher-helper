package com.teacher.teacherhelper.config

import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.Bot
import net.mamoe.mirai.BotFactory
import net.mamoe.mirai.auth.BotAuthorization
import net.mamoe.mirai.event.EventChannel
import net.mamoe.mirai.event.events.BotEvent
import net.mamoe.mirai.utils.BotConfiguration
import org.springframework.ai.chat.model.ChatModel
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import top.mrxiaom.overflow.BotBuilder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Configuration
class BotConfig(
    @Value("\${qq.account}") private val account: Long,
    private val chatModel: ChatModel
) {

    @Bean
     fun initBot(): Bot {
        return runBlocking {
            BotBuilder.positive("ws://127.0.0.1:3001")
                .token(URLEncoder.encode("Xogc}xNoqjHhpE3M", StandardCharsets.UTF_8.toString()))
                .connect() as Bot
        }
    }

    @Bean
    fun initEvent(bot: Bot):EventChannel<BotEvent> = bot.eventChannel
}