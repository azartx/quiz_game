package com.solo4.millionerquiz.model.game

import androidx.compose.runtime.Stable

data class Question(
    val id: Int,
    val questionText: String,
    val imageUrl: String,
    val answers: List<Answer>
) {
    @Stable
    @Transient
    var isAnswered: Boolean = false

    override fun toString(): String {
        return "Question{id=$id, questionText=$questionText, imageUrl=$imageUrl, answers=$answers, isAnswered=$isAnswered}"
    }
}
