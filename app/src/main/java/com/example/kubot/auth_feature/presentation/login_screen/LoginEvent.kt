package com.example.kubot.auth_feature.presentation.login_screen

import com.example.kubot.auth_feature.domain.AuthInfo
import com.example.kubot.core.presentation.util.UiText

sealed interface LoginEvent {
    data class UpdateEmail(val email: String) : LoginEvent
    data class UpdatePassword(val password: String) : LoginEvent
    data class SetIsPasswordVisible(val isPasswordVisible: Boolean) : LoginEvent

    object ValidateEmail : LoginEvent
    object ValidatePassword : LoginEvent

    object ShowInvalidEmailMessage : LoginEvent
    object ShowInvalidPasswordMessage : LoginEvent

    data class Login(val email: String, val password: String) : LoginEvent
    data class SetIsLoading(val isLoading: Boolean) : LoginEvent
    data class LoginSuccess(val authInfo: AuthInfo) : LoginEvent
    data class LoginError(val message: UiText) : LoginEvent

    data class UnknownError(val message: UiText) : LoginEvent
}
