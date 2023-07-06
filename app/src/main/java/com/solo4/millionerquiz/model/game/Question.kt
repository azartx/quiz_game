package com.solo4.millionerquiz.model.game

import androidx.compose.runtime.Stable

data class Question(
    val id: Int,
    val questionText: String,
    val answers: List<Answer>
) {
    @Stable
    @Transient
    var isAnswered: Boolean = false
}
