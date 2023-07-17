package com.solo4.millionerquiz.model

sealed interface ScreenState {
    object Loading : ScreenState
    object Success : ScreenState
    object Failure : ScreenState
    object None : ScreenState
}