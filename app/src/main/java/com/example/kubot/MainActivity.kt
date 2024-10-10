package com.example.kubot

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.kubot.auth_feature.presentation.login_screen.LoginScreen
import com.example.kubot.auth_feature.presentation.splash_screen.MainActivityViewModel
import com.example.kubot.core.presentation.navigation.SetupNavGraph
import com.example.kubot.core.presentation.util.Screens
import com.example.kubot.ui.theme.KubotTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlin.system.exitProcess


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            KubotTheme {
                val nav = rememberNavController()
                LoginScreen(navigator = nav)
                SetupNavGraph(navController = nav)

                val splashState by viewModel.splashState.collectAsState()
                val context = LocalContext.current


                // Load Settings (or initialize them)
                LaunchedEffect(true) {
                    val appSettings =
                        viewModel.appSettingsRepository.getAppSettings()

                    // Confirm the settings file is created and initialized
                    if (!appSettings.isSettingsInitialized) {
                        viewModel.appSettingsRepository.saveIsSettingsInitialized(true)
                    }

                    // Set logged-in user Authentication status
                    viewModel.onSetAuthInfo(appSettings.authInfo)
                }


                // Display any errors
                LaunchedEffect(splashState.error) {
                    if (splashState.error != null) {
                        Toast.makeText(context, splashState.error, Toast.LENGTH_LONG).show()
                        delay(1000)
                        viewModel.onSetAuthInfo(null)
                    }
                }

                if (!splashState.isLoading) {
                    nav.navigate(Screens.Login.route)
                    if (splashState.authInfo != null) {
                        nav.navigate(Screens.Profile.route)
                    } else {
                        nav.navigate(Screens.Login.route)

                    }
                }
            }
        }
    }

    fun exitApp() {
        finish()
        exitProcess(0)
    }
}