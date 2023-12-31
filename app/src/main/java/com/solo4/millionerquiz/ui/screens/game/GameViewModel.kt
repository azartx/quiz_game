package com.solo4.millionerquiz.ui.screens.game

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.solo4.millionerquiz.data.advert.AdvertController
import com.solo4.millionerquiz.data.auth.AuthManager
import com.solo4.millionerquiz.data.repositories.questions.LevelsRepository
import com.solo4.millionerquiz.data.repositories.score.ScoreRepository
import com.solo4.millionerquiz.model.auth.User
import com.solo4.millionerquiz.model.game.Answer
import com.solo4.millionerquiz.model.game.GameScreenState
import com.solo4.millionerquiz.model.game.Question
import com.solo4.millionerquiz.ui.navigation.Routes.Companion.ARG_CURRENT_LEVEL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val authManager: AuthManager,
    private val levelRepository: LevelsRepository,
    private val scoreRepository: ScoreRepository,
    private val adController: AdvertController
) : ViewModel() {
    private val initialQuestion = Question(0, "Loading", "", listOf())

    val currentQuestion = MutableStateFlow(initialQuestion)

    val screenState = mutableStateOf<GameScreenState>(GameScreenState.Loading)

    var questionsCount = 0

    val score = MutableStateFlow(0)

    var rightAnswersCount = 0

    val scoreForEndGameUI = mutableStateOf(0)

    val backFromGameScreenEvent = MutableSharedFlow<Unit>()

    fun initialize() {
        viewModelScope.launch(Dispatchers.IO) {
            val level = levelRepository.getSpecificLevel(getCurrentLevelNumber()) // todo can cause an exceptions
            currentQuestion.value = level.questions.first()
            questionsCount = level.questions.size
            screenState.value = GameScreenState.GameInProgress(level.questions)
            withContext(Dispatchers.Main) {
                adController.loadFullscreenAdvert()
            }
        }
    }

    private fun getCurrentLevelNumber() = savedStateHandle.get<String>(ARG_CURRENT_LEVEL)?.toInt() ?: 1

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

        viewModelScope.launch {
            User.updateLastCompletedLevel(getCurrentLevelNumber() + 1)
            launch {
                scoreRepository.updateUserScore(authManager.authState.value.user, score.value)
            }
            for (number in 0..score.value step 40) {
                scoreForEndGameUI.value = number
                delay(150)
            }
        }
    }

    fun markCurrentQuestionAsAnswered(pickedAnswer: Answer?) {
        viewModelScope.launch {
            currentQuestion.value.isAnswered = true
            if (pickedAnswer == null) return@launch
            if (pickedAnswer == currentQuestion.value.answers.first { it.isRight }) { // picked answer was right
                ++rightAnswersCount
                score.emit(score.value + SCORE_POINTS_RIGHT_ANSWER)
            }
        }
    }

    fun getNumberOfEndGameStars(): Int {
        val needRightQuestionsForOneStar = questionsCount / 3
        var stars = 0
        stars += if (rightAnswersCount > needRightQuestionsForOneStar) 1 else 0
        stars += if (rightAnswersCount > (needRightQuestionsForOneStar * 2)) 1 else 0
        stars += if (rightAnswersCount >= (questionsCount - 1)) 1 else 0
        return stars
    }

    fun backWithAdvert(activity: Activity?) {
        viewModelScope.launch {
            if (activity == null) {
                backFromGameScreenEvent.emit(Unit)
                return@launch
            }
            adController.showFullscreenAd(activity)
            backFromGameScreenEvent.emit(Unit)
        }
    }

    private companion object {
        const val TAG = "GameViewModel"

        const val SCORE_POINTS_RIGHT_ANSWER = 150
    }
}
