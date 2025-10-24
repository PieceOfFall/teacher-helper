package com.teacher.teacherhelper.bot.event

import net.mamoe.mirai.event.Event
import net.mamoe.mirai.event.EventChannel
import net.mamoe.mirai.event.events.BotEvent
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class EventRegistrar(
    private val channel: EventChannel<BotEvent>,
    private val subscribers: List<EventSubscriber<*>>
) {

    @EventListener(ContextRefreshedEvent::class)
    fun registerAll() {
        subscribers.forEach { s ->
            @Suppress("UNCHECKED_CAST")
            channel.subscribeAlways(
                s.eventType,
                handler = (s.subscribe() as suspend Event.(Event) -> Unit)
            )
        }
    }
}