package com.solo4.millionerquiz.model.game

sealed class GameScreenState {
    object Loading : GameScreenState()
    data class GameInProgress(val questions: List<Question>) : GameScreenState()
    object EndGame : GameScreenState()
}
