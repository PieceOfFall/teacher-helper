package com.teacher.teacherhelper.config

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor
import org.springframework.ai.chat.memory.InMemoryChatMemory
import org.springframework.ai.chat.model.ChatModel
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AiConfig(
    private val chatModel: ChatModel
) {

    @Bean
    fun chatMemory():InMemoryChatMemory = InMemoryChatMemory()

    @Bean
    fun chatClient(chatMemory: InMemoryChatMemory): ChatClient = ChatClient.builder(chatModel)
        .defaultAdvisors(listOf(SimpleLoggerAdvisor(),MessageChatMemoryAdvisor(chatMemory)))
        .defaultOptions(DashScopeChatOptions.builder().withTopP(0.7).build())
        .build()

}