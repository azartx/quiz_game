package com.solo4.millionerquiz.ui.screens.profile

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.solo4.millionerquiz.R
import com.solo4.millionerquiz.data.auth.AuthManager
import com.solo4.millionerquiz.data.auth.AuthManager.Companion.DEF_USERNAME
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application, private val authManager: AuthManager) :
    AndroidViewModel(application) {

    val authState = authManager.authState

    var generatedUsername: String = DEF_USERNAME
    val usernameTextField = mutableStateOf(authState.value.user.name)

    val logOutEvent = MutableSharedFlow<Unit>()

    val validationState = MutableSharedFlow<String>()

    fun getUserImage(): String {
        return authManager.authState.value.user.profileImageUrl
    }

    fun continueWithEmail(email: String, password: String) {
        viewModelScope.launch(Dispatchers.Default) {
            val isValid = validateCredentials(email, password)

            if (!isValid) {
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
                    validationState.emit(
                        getApplication<Application>().getString(R.string.failed_to_authorize_by_email)
                    )
                }
            }
        }
    }

    private fun validateCredentials(email: String, password: String): Boolean {
        return true // todo
    }

    fun loginAsAnon() {
        viewModelScope.launch {
            if (!authManager.signInAnonymously(usernameTextField.value.ifBlank { AuthManager.DEF_USERNAME_ANON })) {
                validationState.emit(
                    getApplication<Application>().getString(R.string.failed_to_authorize_as_anonymous)
                )
            }
        }
    }

    fun logOut() {
        viewModelScope.launch {
            authManager.logOut()
            logOutEvent.emit(Unit)
        }
    }

    fun updateUserPhoto(imageUri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            authManager.updateUserPhoto(imageUri)
        }
    }

    fun setNewUsername(newMane: String) {
        viewModelScope.launch {
            authManager.changeUsername(newMane)
        }
    }
}
