package com.solo4.millionerquiz.ui.screens.auth

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.solo4.millionerquiz.App
import com.solo4.millionerquiz.R
import com.solo4.millionerquiz.data.auth.AuthManager
import com.solo4.millionerquiz.data.auth.AuthManager.Companion.DEF_USERNAME_ANON
import com.solo4.millionerquiz.data.settings.language.LanguageManager
import com.solo4.millionerquiz.model.database.PreferredLevelLang
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authManager: AuthManager,
    private val langManager: LanguageManager
) : ViewModel() {

    val authState = authManager.authState.asStateFlow()

    val validationState = MutableSharedFlow<String>()

    var pickedLanguage = mutableStateOf(langManager.levelsLanguage)

    var generatedUsername: String = "Unnamed User"
    val usernameTextField = mutableStateOf("")


    fun signInAsAnon() {
        viewModelScope.launch(Dispatchers.Default) {
            if (!authManager.signInAnonymously(usernameTextField.value.ifBlank { DEF_USERNAME_ANON } )) {
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

            val signInResult = authManager.signInByEmail(
                email,
                password,
                usernameTextField.value.ifBlank { generatedUsername })

            if (!signInResult) {
                val createUserResult = authManager.createUserByEmail(
                    email,
                    password,
                    usernameTextField.value.ifBlank { generatedUsername })

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

    fun generateRandomUsername() {
        viewModelScope.launch(Dispatchers.IO) {
            val namesList = App.app.resources.openRawResource(R.raw.usernames)
                .reader()
                .use { it.readLines() }
            usernameTextField.value = namesList[(0..namesList.size).random()]
                .plus((0..10000).random())
            generatedUsername = usernameTextField.value
        }
    }
}
