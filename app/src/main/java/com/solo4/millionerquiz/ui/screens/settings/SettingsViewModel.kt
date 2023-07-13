package com.solo4.millionerquiz.ui.screens.settings

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.solo4.millionerquiz.data.settings.language.LanguageManager
import com.solo4.millionerquiz.model.database.PreferredLevelLang

class SettingsViewModel(private val langManager: LanguageManager) : ViewModel() {
    var pickedLanguage = mutableStateOf(langManager.levelsLanguage)

    fun changePickedLanguage(newLanguage: PreferredLevelLang) {
        langManager.levelsLanguage = newLanguage
        pickedLanguage.value = newLanguage
    }
}
