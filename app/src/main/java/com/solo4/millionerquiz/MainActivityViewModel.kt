package com.solo4.millionerquiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.solo4.millionerquiz.data.advert.AdvertController
import com.solo4.millionerquiz.data.auth.AuthManager
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainActivityViewModel(private val authManager: AuthManager, private val adController: AdvertController) : ViewModel() {

    val showAdState = adController.showAdBannerState.asStateFlow()

    fun isUserAuthenticated() = authManager.isAuthenticated() // todo rework to mutable state
    fun handleAdBannerVisibility(route: String) {
        viewModelScope.launch {
            adController.handleAdBannerVisibility(route)
        }
    }
}
