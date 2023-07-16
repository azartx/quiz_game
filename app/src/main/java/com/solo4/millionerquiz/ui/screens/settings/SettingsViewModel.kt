package com.solo4.millionerquiz.ui.screens.settings

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.solo4.millionerquiz.data.MediaManager
import com.solo4.millionerquiz.data.settings.language.LanguageManager
import com.solo4.millionerquiz.model.database.PreferredLevelLang

class SettingsViewModel(
    private val langManager: LanguageManager,
    private val mediaManager: MediaManager
) : ViewModel() {
    var pickedLanguage = mutableStateOf(langManager.levelsLanguage)

    val isSoundsEnabled = mutableStateOf(mediaManager.isSoundsEnabled)
    val isMusicEnabled = mutableStateOf(mediaManager.isMusicEnabled)

    fun changePickedLanguage(newLanguage: PreferredLevelLang) {
        langManager.levelsLanguage = newLanguage
        pickedLanguage.value = newLanguage
    }

    fun changeSoundsState(isEnabled: Boolean) {
        mediaManager.isSoundsEnabled = isEnabled
        isSoundsEnabled.value = isEnabled
    }

    fun changeMusicState(isEnabled: Boolean) {
        mediaManager.isMusicEnabled = isEnabled
        isMusicEnabled.value = isEnabled
    }
}
