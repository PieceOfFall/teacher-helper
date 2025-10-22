package com.teacher.teacherhelper.model

data class ResponseData<T>(val code: Int, val message: String, val data: T? = null)
