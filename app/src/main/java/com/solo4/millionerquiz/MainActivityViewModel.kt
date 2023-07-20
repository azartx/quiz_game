package com.solo4.millionerquiz

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.solo4.millionerquiz.data.advert.AdvertController
import com.solo4.millionerquiz.data.auth.AuthManager
import com.solo4.millionerquiz.data.database.RemoteDBFetcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainActivityViewModel(
    private val authManager: AuthManager,
    private val adController: AdvertController,
    private val remoteDBFetcher: RemoteDBFetcher
) : ViewModel() {

    val showAdState = adController.showAdBannerState.asStateFlow()

    val keepOnSplashScreen = MutableStateFlow(true)

    init {
        viewModelScope.launch {
            val remoteFetchResult = remoteDBFetcher.checkRemoteDatabaseVersion()
            if (!remoteFetchResult) Log.e("MainActivityViewModel", "Fetch levels on splash failed!")
            keepOnSplashScreen.emit(false)
        }
    }

    fun isUserAuthenticated() = authManager.isAuthenticated() // todo rework to mutable state
    fun handleAdBannerVisibility(route: String) {
        viewModelScope.launch {
            adController.handleAdBannerVisibility(route)
        }
    }
}
