package com.example.kubot.auth_feature.presentation.splash_screen

import com.example.kubot.auth_feature.domain.AuthInfo

data class SplashState(
    val authInfo: AuthInfo? = null,
    val isLoading: Boolean = true,
    val error: String? = null,
)