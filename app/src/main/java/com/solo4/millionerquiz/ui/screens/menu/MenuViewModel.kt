package com.solo4.millionerquiz.ui.screens.menu

import androidx.lifecycle.ViewModel
import com.solo4.millionerquiz.data.auth.AuthManager

class MenuViewModel(
    private val authManager: AuthManager
) : ViewModel() {

    val authState = authManager.authState
}
