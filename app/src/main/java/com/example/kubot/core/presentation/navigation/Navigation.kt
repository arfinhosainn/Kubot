package com.example.kubot.core.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.kubot.auth_feature.presentation.login_screen.LoginScreen
import com.example.kubot.auth_feature.presentation.login_screen.Test
import com.example.kubot.auth_feature.presentation.register_screen.RegisterScreen
import com.example.kubot.core.presentation.util.Screens


@Composable
fun SetupNavGraph(
    navController: NavHostController,
) {

    NavHost(navController = navController, startDestination = Screens.Login.route) {
        composable(route = Screens.Profile.route) {
            Test()
        }

        composable(route = Screens.Login.route) {
            LoginScreen(navigator = navController)
        }

        composable(route = Screens.Register.route) {
            RegisterScreen(navigator = navController)
        }

    }

}