package com.solo4.millionerquiz

import android.app.Activity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.solo4.millionerquiz.data.MediaManager
import com.solo4.millionerquiz.debug.AddLevelScreen
import com.solo4.millionerquiz.ui.components.AdvertBanner
import com.solo4.millionerquiz.ui.navigation.AppNavHost
import com.solo4.millionerquiz.ui.navigation.Routes
import com.solo4.millionerquiz.ui.screens.auth.AuthScreen
import com.solo4.millionerquiz.ui.theme.QuizGameTheme
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModel()
    private val mediaManager: MediaManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ********full screen*********
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        // ****************************
        installSplashScreen().setKeepOnScreenCondition { viewModel.keepOnSplashScreen.value }
        lifecycle.addObserver(mediaManager) // initialize app music
        val navBarSize = try {
            resources.getDimensionPixelSize(
                resources.getIdentifier("navigation_bar_height", "dimen", "android")
            )
        } catch (e: Exception) {
            0
        }
        setContent {
            val navController = rememberNavController()
            val showAdState by viewModel.showAdState.collectAsState()

            DisposableEffect(key1 = "", effect = {
                val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
                    viewModel.handleAdBannerVisibility(
                        destination.label?.toString() ?: Routes.MenuScreenRoute.name
                    )
                }
                navController.addOnDestinationChangedListener(listener)
                onDispose { navController.removeOnDestinationChangedListener(listener) }
            })

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
                    if (showAdState) {
                        Box(modifier = Modifier.padding(20.dp).fillMaxWidth()) {
                            AdvertBanner(modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter))
                        }
                    }
                }
            }
        }
    }
}
