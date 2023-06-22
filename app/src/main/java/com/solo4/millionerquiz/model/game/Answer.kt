package com.solo4.millionerquiz.model.game

data class Answer(
    val id: Int,
    val text: String,
    val isRight: Boolean = false
)
