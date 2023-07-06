package com.solo4.millionerquiz.ui.screens.picklevel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.solo4.millionerquiz.data.repositories.questions.LevelsRepository
import com.solo4.millionerquiz.ui.navigation.Routes.Companion.ARG_CURRENT_LEVEL
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

    fun getCurrentLevel(): Int = savedStateHandle
        .get<String>(ARG_CURRENT_LEVEL)
        ?.toInt()
        ?.minus(1)
        ?: 0
}
