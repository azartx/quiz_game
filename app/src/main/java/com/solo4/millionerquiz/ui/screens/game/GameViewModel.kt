package com.solo4.millionerquiz.ui.screens.game

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.solo4.millionerquiz.data.repositories.questions.QuestionRepository
import com.solo4.millionerquiz.model.game.GameScreenState
import com.solo4.millionerquiz.model.game.Question
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameViewModel(private val quizRepository: QuestionRepository) : ViewModel() {
    private val initialQuestion = Question(0, "Loading", listOf())

    val currentQuestion = mutableStateOf(initialQuestion)

    val screenState = mutableStateOf<GameScreenState>(GameScreenState.Loading)

    fun initialize() {
        viewModelScope.launch(Dispatchers.IO) {
            val questionsResult = quizRepository.getQuestionsPool()
            if (questionsResult.isEmpty()) {
                throw IllegalStateException("Must not be empty!")
            }

            currentQuestion.value = questionsResult.first()
            screenState.value = GameScreenState.GameInProgress(questionsResult)
        }
    }

    fun nextQuestion() {
        currentQuestion.value =
            (screenState.value as? GameScreenState.GameInProgress)
                ?.questions?.getOrElse(currentQuestion.value.id) {
                    screenState.value = GameScreenState.EndGame
                    return
                } ?: return
    }
}
