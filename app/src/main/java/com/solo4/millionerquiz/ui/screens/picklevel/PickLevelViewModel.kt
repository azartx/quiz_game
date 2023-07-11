package com.solo4.millionerquiz.ui.screens.picklevel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.solo4.millionerquiz.data.repositories.questions.LevelsRepository
import com.solo4.millionerquiz.model.auth.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PickLevelViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val levelsRepository: LevelsRepository
) : ViewModel() {

    val levelsCount = mutableStateOf(0)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            levelsCount.value = levelsRepository.getLevelsCount()
        }
    }

    val currentLevel = User.currentLevel

    fun isLevelClickable(number: Int): Boolean {
        return number <= currentLevel
    }
}
