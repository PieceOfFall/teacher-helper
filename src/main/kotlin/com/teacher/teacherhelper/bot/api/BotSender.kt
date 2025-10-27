package com.teacher.teacherhelper.bot.api

import com.teacher.teacherhelper.annotation.KSlf4j
import com.teacher.teacherhelper.annotation.logger
import net.mamoe.mirai.Bot
import org.springframework.stereotype.Component

@KSlf4j
@Component
@Suppress("unused")
class BotSender(
    private val bot: Bot
) {

    private val log = logger()

    suspend fun sendMsg(qq: Long, msg: String): Result<Unit> = runCatching {
        val friend = bot.getFriend(qq) ?: error("未找到账号为 $qq 的好友")
        friend.sendMessage(msg)
        Unit
    }.onFailure { e ->
        log.error("向账号 {} 发送消息失败：{}", qq, e.message, e)
    }

    suspend fun sendGroupMsg(groupNo: Long, msg: String): Result<Unit> = runCatching {
        val group = bot.getGroup(groupNo) ?: error("未找到群号为 $groupNo 的群聊")
        group.sendMessage(msg)
        Unit
    }.onFailure { e ->
        log.error("向qq群 {} 发送消息失败：{}", groupNo, e.message, e)
    }
}