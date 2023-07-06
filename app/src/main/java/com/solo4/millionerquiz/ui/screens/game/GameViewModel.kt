package com.solo4.millionerquiz.ui.screens.game

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.solo4.millionerquiz.data.repositories.questions.LevelsRepository
import com.solo4.millionerquiz.model.game.GameScreenState
import com.solo4.millionerquiz.model.game.Question
import com.solo4.millionerquiz.ui.navigation.Routes.Companion.ARG_CURRENT_LEVEL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GameViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val levelRepository: LevelsRepository
) : ViewModel() {
    private val initialQuestion = Question(0, "Loading", listOf())

    val currentQuestion = mutableStateOf(initialQuestion)

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
        currentQuestion.value =
            (screenState.value as? GameScreenState.GameInProgress)
                ?.questions?.getOrElse(currentQuestion.value.id) {
                    screenState.value = GameScreenState.EndGame
                    return
                } ?: return
    }
}
