package com.teacher.teacherhelper.config

import net.mamoe.mirai.Bot
import net.mamoe.mirai.BotFactory
import net.mamoe.mirai.auth.BotAuthorization
import net.mamoe.mirai.utils.BotConfiguration
import org.springframework.ai.chat.model.ChatModel
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BotConfig(
    @Value("\${qq.account}") private val account:Long,
    private val chatModel: ChatModel
) {

    @Bean
    fun initBot(): Bot {
        return BotFactory.newBot(account, BotAuthorization.byQRCode()) {
            protocol = BotConfiguration.MiraiProtocol.MACOS
            fileBasedDeviceInfo()
        }
    }
}