package com.example.kubot.core.presentation.util

sealed class Screens (val route: String){
    object Login: Screens(route = "login_screen")
    object Register: Screens(route = "Register_screen")
    object Profile: Screens(route = "profile_screen")

}