package com.example.kubot.core.data.settings

import com.example.kubot.auth_feature.domain.AuthInfo
import kotlinx.serialization.Serializable

@Serializable
data class AppSettings(
    val authInfo: AuthInfo? = null,
    var isSettingsInitialized: Boolean = false  // allows us to check if the settings is loaded
)