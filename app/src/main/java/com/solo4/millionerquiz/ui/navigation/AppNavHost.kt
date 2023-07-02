package com.solo4.millionerquiz.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.solo4.millionerquiz.ui.screens.auth.AuthScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Routes.AuthScreenRoute.name) { AuthScreen(navHostController = navController) }
        /*composable(
            route = "$CLASSES_LIST_SCREEN/{$SUBJECT_ID}",
            arguments = listOf(navArgument(SUBJECT_ID) { type = NavType.StringType })
        ) { PickClassScreen(navHostController = navController) }
        composable(
            route = "$LITERATURE_LIST_SCREEN/{$SUBJECT_ID}/{$CLASS_NUMBER}",
            arguments = listOf(
                navArgument(SUBJECT_ID) { type = NavType.StringType },
                navArgument(CLASS_NUMBER) { type = NavType.StringType }
            )
        ) { LiteratureScreen(navHostController = navController) }
        composable(
            route = "$TITLE_SCREEN/{$BOOK_NAME}",
            arguments = listOf(navArgument(BOOK_NAME) { type = NavType.StringType })
        ) { TitleScreen(navHostController = navController) }
        composable(
            route = "$READ_BOOK_SCREEN/{$BOOK_NAME}/{$BOOK_URL}",
            arguments = listOf(
                navArgument(BOOK_NAME) { type = NavType.StringType },
                navArgument(BOOK_URL) { type = NavType.StringType }
            )
        ) { ReadBookScreen(navHostController = navController) }*/
    }
}