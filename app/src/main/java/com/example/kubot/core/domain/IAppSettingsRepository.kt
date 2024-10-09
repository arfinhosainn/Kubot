package com.example.kubot.core.domain

import androidx.datastore.core.DataStore
import com.example.kubot.auth_feature.domain.AuthInfo
import com.example.kubot.core.data.settings.AppSettings

interface IAppSettingsRepository {
    abstract val dataStore: DataStore<AppSettings>

    suspend fun saveAuthInfo(authInfo: AuthInfo)

    suspend fun getAppSettings(): AppSettings
    suspend fun saveIsSettingsInitialized(firstTime: Boolean)
}