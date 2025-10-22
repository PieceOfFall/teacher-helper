package com.teacher.teacherhelper.config

import com.teacher.teacherhelper.annotation.KSlf4j
import com.teacher.teacherhelper.annotation.logger
import com.teacher.teacherhelper.model.ResponseData
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@KSlf4j
@RestControllerAdvice
class GlobalExceptionHandler {

    private val log = logger()

    @ExceptionHandler(IllegalArgumentException::class)
    fun handler(e: IllegalArgumentException): ResponseData<Unit> {
        log.warn("非法请求参数:-------------->{}", e.message)
        return ResponseData(HttpServletResponse.SC_BAD_REQUEST, "非法请求参数")
    }

    @ExceptionHandler(Exception::class)
    fun handler(e: Exception): ResponseData<Unit> {
        log.warn("服务异常:-------------->{}", e.message)
        return ResponseData(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "非法请求参数")
    }
}