package com.teacher.teacherhelper.utils.string

import com.teacher.teacherhelper.utils.model.TableModel
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import org.commonmark.ext.gfm.tables.*
import org.commonmark.node.Node
import org.commonmark.parser.Parser
import org.commonmark.renderer.text.TextContentRenderer
import org.scilab.forge.jlatexmath.TeXConstants
import org.springframework.stereotype.Component
import java.util.function.Consumer
import org.scilab.forge.jlatexmath.TeXFormula
import java.awt.AlphaComposite
import java.awt.Color
import java.awt.Insets
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import javax.imageio.ImageIO
import javax.swing.JLabel


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

    fun processLatex(s: String, imgConsumer: Consumer<ByteArray>): String {
        val latexToPngByte = latexToPngByte(s)
        val fileName = "latex${System.nanoTime()}"
        File("$fileName.png").toExternalResource()
        return s;
    }

    fun latexToPngByte(
        latex: String,
        fontSize: Float = 20f,
        padding: Int = 8,
        bgColor: Color? = null, // null = 透明背景；否则不透明背景色
    ): ByteArray {
        // 1) 解析公式（注意：JLaTeXMath是“数学LaTeX”，传入不要带 \[ \] / $$ 的外层分隔）
        val formula = TeXFormula(latex)

        // 2) 生成图标
        val icon = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, fontSize)
        icon.insets = Insets(padding, padding, padding, padding)

        // 3) 画布
        val w = icon.iconWidth
        val h = icon.iconHeight
        val imgType = if (bgColor == null) BufferedImage.TYPE_4BYTE_ABGR else BufferedImage.TYPE_INT_RGB
        val img = BufferedImage(w, h, imgType)
        val g2 = img.createGraphics()

        // 抗锯齿 & 高质量
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)

        // 背景
        if (bgColor == null) {
            g2.composite = AlphaComposite.Clear
            g2.fillRect(0, 0, w, h)
            g2.composite = AlphaComposite.SrcOver
        } else {
            g2.color = bgColor
            g2.fillRect(0, 0, w, h)
        }

        // 4) 绘制
        icon.paintIcon(JLabel(), g2, 0, 0)
        g2.dispose()

        val byteArrOutStream = ByteArrayOutputStream()
        ImageIO.write(img, "png", byteArrOutStream) // ← 转为字节数组
        return byteArrOutStream.toByteArray()
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