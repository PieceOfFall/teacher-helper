package com.teacher.teacherhelper.config

import org.commonmark.parser.Parser
import org.commonmark.renderer.text.TextContentRenderer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MarkDownConfig {

    @Bean
    fun parser(): Parser = Parser.builder().build()

    @Bean
    fun render(): TextContentRenderer = TextContentRenderer.builder().build()
}