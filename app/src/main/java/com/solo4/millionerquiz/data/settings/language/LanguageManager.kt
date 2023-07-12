package com.solo4.millionerquiz.data.settings.language

import android.content.SharedPreferences
import androidx.core.content.edit
import com.solo4.millionerquiz.model.database.PreferredLevelLang
import java.util.Locale

class LanguageManager(private val sharedPreferences: SharedPreferences) {

    var levelsLanguage: PreferredLevelLang
        get() = PreferredLevelLang.valueOfByLevelDbName(
            sharedPreferences.getString(LEVELS_LANG, getDefaultQuestionLanguage().levelsDbName)
                ?: PreferredLevelLang.en.levelsDbName
        )
        set(value) {
            sharedPreferences.edit { putString(LEVELS_LANG, value.levelsDbName) }
        }

    private fun getDefaultQuestionLanguage(): PreferredLevelLang {
        return when (Locale.getDefault().language) {
            "en" -> PreferredLevelLang.en
            "ru" -> PreferredLevelLang.ru
            else -> PreferredLevelLang.en
        }
    }

    companion object {
        const val LEVELS_LANG = "levelsLanguage"
    }
}
