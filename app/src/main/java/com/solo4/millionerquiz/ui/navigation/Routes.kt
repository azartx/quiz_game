package com.solo4.millionerquiz.ui.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.solo4.millionerquiz.data.advert.routeWithoutAD

sealed class Routes {
    open val name: String = this::class.java.simpleName
    open val route: String = name
    object AuthScreenRoute : Routes()
    object MenuScreenRoute : Routes()
    object SettingsScreenRoute : Routes()
    object ProfileScreenRoute : Routes()
    object AboutScreenRoute : Routes()
    object ScoreScreenRoute : Routes()
    object DevScreenRoute : Routes()
    object PickLevelRoute : Routes(), Argumentative {
        override val name: String = this::class.java.simpleName.routeWithoutAD()
        override val route: String = "$name/{$ARG_CURRENT_LEVEL}"
        override val args: List<NamedNavArgument> = listOf(
            navArgument(ARG_CURRENT_LEVEL) { NavType.IntType }
        )

        override fun nameWithArgs(vararg args: String): String {
            var nameWithArgs = name
            args.forEach { nameWithArgs += "/$it" }
            return nameWithArgs
        }
    }
    object GameRoute : Routes(), Argumentative {
        override val name: String = this::class.java.simpleName.routeWithoutAD()
        override val route: String = "$name/{$ARG_CURRENT_LEVEL}"
        override val args: List<NamedNavArgument> = listOf(
            navArgument(ARG_CURRENT_LEVEL) { NavType.IntType }
        )

        override fun nameWithArgs(vararg args: String): String {
            var nameWithArgs = name
            args.forEach { nameWithArgs += "/$it" }
            return nameWithArgs
        }
    }

    companion object {
        const val ARG_CURRENT_LEVEL = "ARG_CURRENT_LEVEL"
    }
}

interface Argumentative {
    val args: List<NamedNavArgument>
    fun nameWithArgs(vararg args: String): String
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
