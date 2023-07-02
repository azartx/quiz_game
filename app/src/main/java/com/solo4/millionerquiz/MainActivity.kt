package com.solo4.millionerquiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.solo4.millionerquiz.ui.navigation.AppNavHost
import com.solo4.millionerquiz.ui.navigation.Routes
import com.solo4.millionerquiz.ui.screens.auth.AuthScreen
import com.solo4.millionerquiz.ui.screens.picklevel.PickLevelScreen
import com.solo4.millionerquiz.ui.theme.QuizGameTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition {
            false
        }
        setContent {
            val navController = rememberNavController()
            QuizGameTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavHost(
                        navController = navController,
                        startDestination = if (viewModel.isUserAuthenticated())
                            Routes.MenuScreenRoute.name else Routes.AuthScreenRoute.name
                    )
                }
            }
        }
    }
}
