package com.solo4.millionerquiz.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.solo4.millionerquiz.data.auth.AuthManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val authManager: AuthManager) : ViewModel() {

    val authState = authManager.authState.asStateFlow()

    fun signInAsAnon() {
        viewModelScope.launch(Dispatchers.Default) {
            val isSuccess = authManager.signInAnonymously()
        }
    }

    fun signInByEmail(email: String, password: String) {
        viewModelScope.launch(Dispatchers.Default) {
            val isValid = validateCredentials(email, password)

            if (!isValid) {
                // todo error
                return@launch
            }

            val signInResult = authManager.signInByEmail(email, password)

            if (!signInResult) {
                if (authManager.isUserAnonymous()) {
                    authManager.createUserByEmailFromAnon(email, password)
                } else {
                    authManager.createUserByEmail(email, password)
                }
            }
        }
    }

    private fun validateCredentials(email: String, password: String): Boolean {
        return true // todo
    }

    fun logOut() {
        viewModelScope.launch {
            authManager.logOut()
        }
    }
}
