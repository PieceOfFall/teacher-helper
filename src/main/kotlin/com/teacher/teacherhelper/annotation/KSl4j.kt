package com.teacher.teacherhelper.annotation

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class KSlf4j(val clazz: KClass<*> = Any::class)

fun <T : Any> T.logger(): Logger = LoggerFactory.getLogger(this::class.java)