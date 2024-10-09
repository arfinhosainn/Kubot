package com.example.kubot.auth_feature.presentation.login_screen

import com.example.kubot.auth_feature.domain.AuthInfo
import com.example.kubot.core.presentation.util.UiText


data class LoginState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,

    val isInvalidEmail: Boolean = false,
    val isInvalidEmailMessageVisible: Boolean = false,
    val isInvalidPassword: Boolean = false,
    val isInvalidPasswordMessageVisible: Boolean = false,

    val isLoading: Boolean = false,

    val statusMessage: UiText? = null,
    val errorMessage: UiText? = null,

    val authInfo: AuthInfo? = null,  // when not-null, the user is logged in
)