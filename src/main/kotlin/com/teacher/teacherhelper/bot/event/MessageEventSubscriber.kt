package com.teacher.teacherhelper.bot.event

import com.teacher.teacherhelper.bot.api.BotSender
import com.teacher.teacherhelper.utils.string.FlowStringUtil
import com.teacher.teacherhelper.utils.string.MarkDownService
import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.asFlow
import net.mamoe.mirai.event.EventChannel
import net.mamoe.mirai.event.events.BotEvent
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.MessageSourceKind
import net.mamoe.mirai.message.data.source
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component


@Component
class MessageEventSubscriber(
    @Value("\${ai.prompt}") private val prompt: String,
    private val eventChannel: EventChannel<BotEvent>,
    private val chatClient: ChatClient,
    private val botSender: BotSender,
    private val markDownService: MarkDownService
) : EventSubscriber {

    // 受管作用域（随 Bean 生命周期取消）
    private val job = SupervisorJob()
    private val scope = CoroutineScope(job + Dispatchers.Default)

    @PostConstruct
    override fun subscribe() {
        eventChannel.subscribeAlways<MessageEvent> { event ->
            if (event.message.source.kind != MessageSourceKind.FRIEND) return@subscribeAlways

            val flux = chatClient.prompt()
                .system(prompt)
                .user(event.message.contentToString())
                .advisors {
                    it.param(CHAT_MEMORY_CONVERSATION_ID_KEY, event.sender.id)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10)
                }
                .stream()
                .content() // Flux<String>

            scope.launch {
                flux.asFlow() // -> Flow<String>
                    .let(FlowStringUtil::splitMarkdownByHeader) // 按标题分块
                    .map(markDownService::stripMarkdownLight)  // 去掉 #、* 等标记
                    .filter { it.isNotBlank() }
                    .collect { line -> botSender.sendMsg(event.sender.id, line) }
            }
        }
    }

    @PreDestroy
    fun shutdown() = job.cancel()

}


