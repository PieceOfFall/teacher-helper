package com.teacher.teacherhelper.utils.model

data class TableModel(
    val headers: List<Cell>,
    val aligns: List<Alignment>,
    val rows: List<List<Cell>>
) {
    data class Cell(val text: String)
    enum class Alignment { LEFT, CENTER, RIGHT }
}