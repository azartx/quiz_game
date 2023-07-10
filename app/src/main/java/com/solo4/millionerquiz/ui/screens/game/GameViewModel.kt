package com.solo4.millionerquiz.ui.screens.game

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.solo4.millionerquiz.data.repositories.questions.LevelsRepository
import com.solo4.millionerquiz.model.game.GameScreenState
import com.solo4.millionerquiz.model.game.Question
import com.solo4.millionerquiz.ui.navigation.Routes.Companion.ARG_CURRENT_LEVEL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class GameViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val levelRepository: LevelsRepository
) : ViewModel() {
    private val initialQuestion = Question(0, "Loading", listOf())

    val currentQuestion = MutableStateFlow(initialQuestion)

    val screenState = mutableStateOf<GameScreenState>(GameScreenState.Loading)

    fun initialize() {
        viewModelScope.launch(Dispatchers.IO) {
            val currentLevel = savedStateHandle.get<String>(ARG_CURRENT_LEVEL)?.toInt() ?: 1
            val level = levelRepository.getSpecificLevel(currentLevel) // todo can cause an exceptions
            currentQuestion.value = level.questions.first()
            screenState.value = GameScreenState.GameInProgress(level.questions)
        }
    }

    fun nextQuestion() {
        val questions = (screenState.value as? GameScreenState.GameInProgress)?.questions ?: run {
            Log.e(TAG, "Illegal screen state")
            return
        }
        val currentQuestionIndex = questions.indexOfFirst { it == currentQuestion.value }
        if (currentQuestionIndex == -1) {
            endGame()
            throw IllegalArgumentException("Current question is not found")
        }
        val newQuestion = questions.getOrElse(currentQuestionIndex + 1) {
            endGame()
            Log.e(TAG, "Game state is EndGame now")
            return
        }
        viewModelScope.launch { currentQuestion.emit(newQuestion) }
    }

    private fun endGame() {
        screenState.value = GameScreenState.EndGame
        // todo some end game actions
    }

    private companion object {
        const val TAG = "GameViewModel"
    }
}
