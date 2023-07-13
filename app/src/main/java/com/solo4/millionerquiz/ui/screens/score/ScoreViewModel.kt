package com.solo4.millionerquiz.ui.screens.score

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.solo4.millionerquiz.data.auth.AuthManager
import com.solo4.millionerquiz.data.repositories.score.ScoreRepository
import com.solo4.millionerquiz.model.score.UserScoreItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ScoreViewModel(
    authManager: AuthManager,
    private val scoreRepo: ScoreRepository
) : ViewModel() {
    val userScoresList = MutableStateFlow<List<UserScoreItem>>(listOf())

    val currentUser = authManager.authState.value.user

    init {
        viewModelScope.launch(Dispatchers.IO) {
            userScoresList.emit(scoreRepo.getUsersScore().sortedByDescending { it.score })
        }
    }
}
