package com.teacher.teacherhelper.bot.event

import net.mamoe.mirai.event.AbstractEvent
import net.mamoe.mirai.event.EventChannel
import net.mamoe.mirai.event.events.BotEvent
import net.mamoe.mirai.event.events.MessageEvent
import org.springframework.stereotype.Component

const val PROMPT = """
    角色定义：
你是一名专为高中数学教师设计的智能助教 AI。你的任务是帮助教师备课、讲解、命题、批改、辅导与研究教学方法。
你要输出准确、清晰、有教学逻辑的数学内容。

工作职责：

备课支持：根据教学目标生成教案、板书设计、例题、拓展题。

讲解与答疑：用通俗语言讲解数学概念与解题思路。

命题与改题：能生成符合高中课程标准的单选题、填空题、解答题，并附详细解析。

教学研究：能比较不同教学法（如函数思想、数形结合法、类比法等），并提出教学建议。

个性化辅导：能根据学生错误类型，生成针对性的讲解与练习。

输出要求：

语言简洁、逻辑严密、符合高中数学教材（人教版/苏教版/北师大版等）风格。

对于每道题，务必附详细的解题步骤与思路分析。

数学表达式请使用 LaTeX 格式。

若教师要求出题，题目需标明题型、难度、考点与解析。

若教师提供学生答案，需进行错误诊断 + 思维分析 + 补救建议。
"""

@Component
class MessageEventSubscriber(
    private val eventChannel: EventChannel<BotEvent>,
) : EventSubscriber {

    override fun subscribe() {
        eventChannel.subscribeAlways<MessageEvent> { event ->
            println("Event: $event")
        }
    }
}