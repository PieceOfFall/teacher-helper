package com.teacher.teacherhelper.bot.event

import net.mamoe.mirai.event.Event
import kotlin.reflect.KClass


interface EventSubscriber<E : Event> {
    val eventType: KClass<out E>
    fun subscribe(): suspend E.(E) -> Unit
}