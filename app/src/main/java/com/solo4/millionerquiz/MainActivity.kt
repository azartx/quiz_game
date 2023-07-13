package com.solo4.millionerquiz

import android.app.Activity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.solo4.millionerquiz.debug.AddLevelScreen
import com.solo4.millionerquiz.ui.navigation.AppNavHost
import com.solo4.millionerquiz.ui.navigation.Routes
import com.solo4.millionerquiz.ui.screens.auth.AuthScreen
import com.solo4.millionerquiz.ui.theme.QuizGameTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ********full screen*********
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        // ****************************
        installSplashScreen().setKeepOnScreenCondition { false }
        val navBarSize = try {
            resources.getDimensionPixelSize(
                resources.getIdentifier("navigation_bar_height", "dimen", "android")
            )
        } catch (e: Exception) {
            0
        }
        setContent {
            val navController = rememberNavController()
            QuizGameTheme {
                val view = LocalView.current
                val window = (view.context as Activity).window
                val darkTheme = isSystemInDarkTheme()

                if (!view.isInEditMode) {
                    SideEffect {
                        WindowCompat.setDecorFitsSystemWindows(window, false)
                        window.statusBarColor = Color.White.copy(alpha = 0f).toArgb()
                        WindowCompat.getInsetsController(window, view)
                            .isAppearanceLightStatusBars = !darkTheme
                        WindowCompat.getInsetsController(window, view)
                            .isAppearanceLightNavigationBars = !darkTheme
                    }
                }
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(WindowInsets(bottom = navBarSize)),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        painter = painterResource(id = R.drawable.background),
                        contentDescription = "Background",
                        contentScale = ContentScale.Crop
                    )
                    if (BuildConfig.isFullApp) {
                        AppNavHost(
                            navController = navController,
                            startDestination = if (viewModel.isUserAuthenticated())
                                Routes.MenuScreenRoute.name else Routes.AuthScreenRoute.name
                        )
                    } else {
                        if (viewModel.isUserAuthenticated()) AddLevelScreen() else AuthScreen()
                    }
                }
            }
        }
    }
}
