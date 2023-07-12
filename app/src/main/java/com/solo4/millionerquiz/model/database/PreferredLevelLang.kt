package com.solo4.millionerquiz.model.database

enum class PreferredLevelLang(val levelsDbName: String) {
    ru("levels_ru"), en("levels");

    companion object {
        fun valueOfByLevelDbName(name: String): PreferredLevelLang = when (name) {
            "levels_ru" -> ru
            "levels" -> en
            else -> throw IllegalArgumentException("Illegal argument")
        }
    }
}
