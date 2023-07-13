package com.solo4.millionerquiz.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.solo4.millionerquiz.debug.AddLevelScreen
import com.solo4.millionerquiz.ui.screens.auth.AuthScreen
import com.solo4.millionerquiz.ui.screens.game.GameScreen
import com.solo4.millionerquiz.ui.screens.menu.MenuScreen
import com.solo4.millionerquiz.ui.screens.picklevel.PickLevelScreen
import com.solo4.millionerquiz.ui.screens.profile.ProfileScreen
import com.solo4.millionerquiz.ui.screens.settings.SettingsScreen

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
        composable(Routes.MenuScreenRoute.name) { MenuScreen(navHostController = navController) }
        composable(Routes.SettingsScreenRoute.name) { SettingsScreen(navHostController = navController) }
        composable(Routes.ProfileScreenRoute.name) { ProfileScreen(navHostController = navController) }
        composable(Routes.DevScreenRoute.name) { AddLevelScreen() }
        composable(
            route = Routes.PickLevelRoute.route,
            arguments = Routes.PickLevelRoute.args
        ) { PickLevelScreen(navHostController = navController) }
        composable(
            route = Routes.GameRoute.route,
            arguments = Routes.GameRoute.args
        ) { GameScreen(navHostController = navController) }
    }
}