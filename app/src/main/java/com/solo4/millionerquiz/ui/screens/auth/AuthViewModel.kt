package com.solo4.millionerquiz.ui.screens.auth

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.solo4.millionerquiz.data.auth.AuthManager
import com.solo4.millionerquiz.data.settings.language.LanguageManager
import com.solo4.millionerquiz.model.database.PreferredLevelLang
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val authManager: AuthManager, private val langManager: LanguageManager) : ViewModel() {

    val authState = authManager.authState.asStateFlow()

    val validationState = MutableSharedFlow<String>()

    var pickedLanguage = mutableStateOf(langManager.levelsLanguage)

    fun signInAsAnon() {
        viewModelScope.launch(Dispatchers.Default) {
            if (!authManager.signInAnonymously()) {
                validationState.emit("Failed to authorize as anonymous.")
            }
        }
    }

    fun signInByEmail(email: String, password: String) {
        viewModelScope.launch(Dispatchers.Default) {
            val isValid = validateCredentials(email, password)

            if (!isValid) {
                validationState.emit("Invalid email or password")
                return@launch
            }

            val signInResult = authManager.signInByEmail(email, password)

            if (!signInResult) {
                val createUserResult = authManager.createUserByEmail(email, password)

                if (!createUserResult) {
                    validationState.emit("Failed to authorize by email.")
                }
            }
        }
    }

    private fun validateCredentials(email: String, password: String): Boolean {
        return true // todo
    }

    fun changePickedLanguage(newLanguage: PreferredLevelLang) {
        langManager.levelsLanguage = newLanguage
        pickedLanguage.value = newLanguage
    }
}
