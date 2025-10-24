package com.teacher.teacherhelper.utils.string

import org.commonmark.parser.Parser
import org.commonmark.renderer.text.TextContentRenderer
import org.springframework.stereotype.Component

@Component
class MarkDownService(
    val parser: Parser,
    val renderer: TextContentRenderer
) {

    /**
     * 轻量去 Markdown（不处理链接/引用/代码块等复杂规则）
     */
    fun stripMarkdownLight(s: String): String {
        val doc = parser.parse(s)
        return renderer.render(doc)
    }
}