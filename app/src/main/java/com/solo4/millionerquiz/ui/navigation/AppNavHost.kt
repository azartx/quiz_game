package com.solo4.millionerquiz.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.NavHost
import androidx.navigation.get
import com.solo4.millionerquiz.debug.AddLevelScreen
import com.solo4.millionerquiz.ui.screens.about.AboutScreen
import com.solo4.millionerquiz.ui.screens.auth.AuthScreen
import com.solo4.millionerquiz.ui.screens.game.GameScreen
import com.solo4.millionerquiz.ui.screens.menu.MenuScreen
import com.solo4.millionerquiz.ui.screens.picklevel.PickLevelScreen
import com.solo4.millionerquiz.ui.screens.profile.ProfileScreen
import com.solo4.millionerquiz.ui.screens.score.ScoreScreen
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
        composable(Routes.AuthScreenRoute) { AuthScreen(navHostController = navController) }
        composable(Routes.MenuScreenRoute) { MenuScreen(navHostController = navController) }
        composable(Routes.SettingsScreenRoute) { SettingsScreen(navHostController = navController) }
        composable(Routes.ProfileScreenRoute) {
            ProfileScreen(navHostController = navController) }
        composable(Routes.AboutScreenRoute) { AboutScreen(navHostController = navController) }
        composable(Routes.ScoreScreenRoute) { ScoreScreen(navHostController = navController) }
        composable(Routes.DevScreenRoute) { AddLevelScreen() }
        composable(
            route = Routes.PickLevelRoute,
            arguments = Routes.PickLevelRoute.args
        ) { PickLevelScreen(navHostController = navController) }
        composable(
            route = Routes.GameRoute,
            arguments = Routes.GameRoute.args
        ) { GameScreen(navHostController = navController) }
    }
}

fun NavGraphBuilder.composable(
    route: Routes,
    label: String = route.name,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable (NavBackStackEntry) -> Unit
) {
    addDestination(
        ComposeNavigator.Destination(provider[ComposeNavigator::class], content).apply {
            this.label = label
            this.route = route.route
            arguments.forEach { (argumentName, argument) ->
                addArgument(argumentName, argument)
            }
            deepLinks.forEach { deepLink ->
                addDeepLink(deepLink)
            }
        }
    )
}