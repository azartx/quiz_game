package com.solo4.millionerquiz.data.repositories.questions

import com.solo4.millionerquiz.model.game.Question

interface QuestionRepository {
    fun getQuestionsPool(): List<Question>
}
