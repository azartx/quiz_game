package com.solo4.millionerquiz.ui.screens.auth

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.solo4.millionerquiz.App
import com.solo4.millionerquiz.R
import com.solo4.millionerquiz.data.auth.AuthManager
import com.solo4.millionerquiz.data.auth.AuthManager.Companion.DEF_USERNAME
import com.solo4.millionerquiz.data.auth.AuthManager.Companion.DEF_USERNAME_ANON
import com.solo4.millionerquiz.data.settings.language.LanguageManager
import com.solo4.millionerquiz.model.ScreenState
import com.solo4.millionerquiz.model.database.PreferredLevelLang
import com.solo4.millionerquiz.ui.base.BaseAndroidVM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authManager: AuthManager,
    private val langManager: LanguageManager
) : BaseAndroidVM() {

    val authState = authManager.authState.asStateFlow()

    val validationState = MutableSharedFlow<String>()

    var pickedLanguage = mutableStateOf(langManager.levelsLanguage)

    var generatedUsername: String = DEF_USERNAME
    val usernameTextField = mutableStateOf("")


    fun signInAsAnon() {
        viewModelScope.launch(Dispatchers.Default) {
            screenState.value = ScreenState.Loading
            if (!authManager.signInAnonymously(usernameTextField.value.ifBlank { DEF_USERNAME_ANON } )) {
                screenState.value = ScreenState.Failure
                validationState.emit(
                    getApplication<Application>().getString(R.string.failed_to_authorize_as_anonymous)
                )
            }
            screenState.value = ScreenState.Success
        }
    }

    fun signInByEmail(email: String, password: String) {
        viewModelScope.launch(Dispatchers.Default) {
            screenState.value = ScreenState.Loading
            val isValid = validateCredentials(email, password)

            if (!isValid) {
                screenState.value = ScreenState.Failure
                validationState.emit(
                    getApplication<Application>().getString(R.string.invalid_email_or_password)
                )
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
                    screenState.value = ScreenState.Failure
                    validationState.emit(
                        getApplication<Application>().getString(R.string.failed_to_authorize_by_email)
                    )
                }
                screenState.value = ScreenState.Success
            }
            screenState.value = ScreenState.Success
        }
    }

    private fun validateCredentials(email: String, password: String): Boolean {
        return email.isNotBlank() &&
                email.matches("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$".toRegex()) &&
                password.isNotBlank()
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
