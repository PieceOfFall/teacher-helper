package com.teacher.teacherhelper.utils.string

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FlowStringUtil {
    companion object {

        /**
         * 将 Markdown 按标题（行首 1~6 个 # + 空格）分块。
         * 每个块从一个标题开始，直到下一个标题（不含）为止。
         * 任何标题前的“前言”会作为单独一块先输出。
         */
        fun splitMarkdownByHeader(src: Flow<String>): Flow<String> = flow {
            val sb = StringBuilder()
            // 行首标题：1~3 个 #，后跟空格；多行匹配
            val header = Regex("(?m)^#{1,3}\\s")

            src.collect { part ->
                sb.append(part)

                while (true) {
                    val first = header.find(sb) ?: break
                    if (first.range.first == 0) {
                        // 缓冲区从标题开始：找“下一个标题”，以确定当前块的结束
                        val next = header.find(sb, first.range.last + 1)
                        if (next != null) {
                            // 输出 [当前标题..下一个标题起点) 作为一个完整块
                            emit(sb.substring(0, next.range.first))
                            sb.delete(0, next.range.first)
                            // 继续循环，处理后续内容
                        } else {
                            // 还没有下一个标题，等待更多数据再凑成块
                            break
                        }
                    } else {
                        // 缓冲开头不是标题，先把前言吐出
                        emit(sb.substring(0, first.range.first))
                        sb.delete(0, first.range.first)
                        // 继续循环，下一轮要么以标题开头，要么还有前言
                    }
                }
            }

            // 流结束：把剩余内容（可能是最后一个标题块或尾巴）吐出
            if (sb.isNotEmpty()) emit(sb.toString())
        }
    }
}