package com.solo4.millionerquiz.ui.navigation

sealed class Routes {
    abstract val name: String
    object AuthScreenRoute : Routes() {
        override val name: String = this::class.java.simpleName
    }
    object MenuScreenRoute : Routes() {
        override val name: String = this::class.java.simpleName
    }
    /*data class LiteratureRoute(val subjectId: String, val classNumber: String) : Routes() {
        override val name: String = this::class.java.simpleName
        override fun toString(): String {
            return "$LEVELS_SCREEN/$subjectId/$classNumber"
        }
    }
    data class TitleRoute(val bookName: String) : Routes() {
        override val name: String = this::class.java.simpleName
        override fun toString(): String {
            return "$GAME_SCREEN/$bookName"
        }
    }*/
}

/*
object RouteConstants {
    // routes
    const val AUTH_SCREEN = "AUTH_SCREEN"
    const val MENU_SCREEN = "MENU_SCREEN"
    const val LEVELS_SCREEN = "LEVELS_SCREEN"
    const val GAME_SCREEN = "GAME_SCREEN"

    // arguments
    const val SUBJECT_ID = "subjectId"
    const val CLASS_NUMBER = "classNumber"
    const val BOOK_NAME = "bookName"
    const val BOOK_URL = "bookUrl"
}*/
