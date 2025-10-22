package com.teacher.teacherhelper.config

import com.teacher.teacherhelper.annotation.KSlf4j
import com.teacher.teacherhelper.annotation.logger
import com.teacher.teacherhelper.model.ResponseData
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@KSlf4j
@RestControllerAdvice
class GlobalExceptionHandler {

    private val log = logger()

    @ExceptionHandler(IllegalArgumentException::class)
    fun handler(e: IllegalArgumentException): ResponseData<Unit> {
        log.warn("非法请求参数:-------------->{}", e.message)
        return ResponseData(400,"非法请求参数")
    }
}