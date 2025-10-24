package com.teacher.teacherhelper.config

import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.EventChannel
import net.mamoe.mirai.event.events.BotEvent
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import top.mrxiaom.overflow.BotBuilder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Configuration
class BotConfig(
    @Value("\${qq.server.address}") private val address: String,
    @Value("\${qq.server.token}") private val token: String
) {
    @Bean
    fun initBot(): Bot {
        return runBlocking {
            BotBuilder.positive("ws://$address")
                .token(URLEncoder.encode(token, StandardCharsets.UTF_8.toString()))
                .connect() as Bot
        }
    }

    @Bean
    fun initEvent(bot: Bot): EventChannel<BotEvent> = bot.eventChannel
}