package com.solo4.millionerquiz

import androidx.lifecycle.ViewModel
import com.solo4.millionerquiz.data.auth.AuthManager

class MainActivityViewModel(private val authManager: AuthManager) : ViewModel() {
    fun isUserAuthenticated() = authManager.isAuthenticated() // todo rework to mutable state
}
