package com.teacher.teacherhelper.utils.string

import com.teacher.teacherhelper.utils.model.TableModel
import org.commonmark.ext.gfm.tables.*
import org.commonmark.node.Node
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

    private fun extractTable(tb: TableBlock): TableModel {
        var head: TableHead? = null
        var body: TableBody? = null

        var child: Node? = tb.firstChild
        while (child != null) {
            when (child) {
                is TableHead -> head = child
                is TableBody -> body = child
            }
            child = child.next
        }

        // 表头
        val headers = mutableListOf<TableModel.Cell>()
        val aligns = mutableListOf<TableModel.Alignment>()
        head?.let { h ->
            var r: Node? = h.firstChild
            while (r != null) {
                if (r is TableRow) {
                    var c: Node? = r.firstChild
                    while (c != null) {
                        if (c is TableCell) {
                            headers += TableModel.Cell(extractPlainText(c))
                            aligns += when (c.alignment) {
                                TableCell.Alignment.CENTER -> TableModel.Alignment.CENTER
                                TableCell.Alignment.RIGHT -> TableModel.Alignment.RIGHT
                                else -> TableModel.Alignment.LEFT
                            }
                        }
                        c = c.next
                    }
                }
                r = r.next
            }
        }

        // 表体
        val rows = mutableListOf<List<TableModel.Cell>>()
        body?.let { b ->
            var r: Node? = b.firstChild
            while (r != null) {
                if (r is TableRow) {
                    val row = mutableListOf<TableModel.Cell>()
                    val current = r
                    var c: Node? = current?.firstChild
                    while (c != null) {
                        val currentC = c
                        if (currentC is TableCell) {

                            row += TableModel.Cell(extractPlainText(currentC))
                        }
                        c = currentC?.next
                    }
                    rows += row
                }
                r = r?.next
            }
        }

        // 对齐列数统一
        while (aligns.size < headers.size) aligns += TableModel.Alignment.LEFT

        return TableModel(headers, aligns, rows)
    }

    private fun extractPlainText(node: Node): String {
        // 用 TextContentRenderer 只对该 cell 渲染纯文本
        return renderer.render(node).trim()
    }


}