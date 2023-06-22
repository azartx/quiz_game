package com.solo4.millionerquiz.model.game

data class Question(
    val id: Int,
    val questionText: String,
    val answers: List<Answer>
)
