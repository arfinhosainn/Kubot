package com.example.kubot.auth_feature.data.repository.local.authDaoImpls

import android.content.Context
import com.example.kubot.auth_feature.data.repository.local.AuthDao
import com.example.kubot.auth_feature.domain.AuthInfo
import com.example.kubot.core.domain.IAppSettingsRepository
import com.example.kubot.core.util.AuthToken
import com.example.kubot.core.util.UserId
import com.example.kubot.core.util.Username
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import javax.inject.Inject

// Uses the DAO pattern to access the Proto Datastore (does not use Room)

class AuthDaoImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val appSettingsRepository: IAppSettingsRepository, //AppSettingsRepositoryImpl
): AuthDao {

    override suspend fun getAuthToken(): AuthToken? {
        return CoroutineScope(Dispatchers.IO).async {
//            context.dataStore.data.first()
            appSettingsRepository.dataStore.data.first()
                .authInfo?.token
        }.await()
    }

    override suspend fun setAuthToken(authToken: AuthToken?) {
//        context.dataStore.updateData { appSettings ->
        appSettingsRepository.dataStore.updateData { appSettings ->
            appSettings.copy(
                authInfo = appSettings.authInfo
                    ?.copy(token = authToken)
            )
        }
    }

    override suspend fun clearAuthToken() {
        appSettingsRepository.dataStore.updateData { appSettings ->
            appSettings.copy(
                authInfo = appSettings.authInfo
                    ?.copy(token = null)
            )
        }
    }

    override suspend fun getAuthUsername(): Username? {
        return CoroutineScope(Dispatchers.IO).async {
            appSettingsRepository.dataStore.data.first()
                .authInfo?.name
        }.await()
    }

    override suspend fun setAuthUsername(username: Username?) {
        appSettingsRepository.dataStore.updateData { appSettings ->
            appSettings.copy(
                authInfo = appSettings.authInfo
                    ?.copy(name = username)
            )
        }
    }

    override suspend fun getAuthUserId(): UserId? {
        return CoroutineScope(Dispatchers.IO).async {
            appSettingsRepository.dataStore.data.first()
                .authInfo?.id
        }.await()
    }

    override suspend fun setAuthUserId(userId: UserId?) {
        appSettingsRepository.dataStore.updateData { appSettings ->
            appSettings.copy(
                authInfo = appSettings.authInfo
                    ?.copy(id = userId)
            )
        }
    }

    override suspend fun getAuthInfo(): AuthInfo? {
        return CoroutineScope(Dispatchers.IO).async {
            appSettingsRepository.dataStore.data.first()
                .authInfo
        }.await()
    }

    override suspend fun setAuthInfo(authInfo: AuthInfo?) {
        appSettingsRepository.dataStore.updateData { appSettings ->
            appSettings.copy(
                authInfo = authInfo
            )
        }
    }

    override suspend fun clearAuthInfo() {
        appSettingsRepository.dataStore.updateData { appSettings ->
            appSettings.copy(
                authInfo = null
            )
        }
    }
}