package com.example.kubot.auth_feature.data.repository.local

import com.example.kubot.auth_feature.domain.AuthInfo
import com.example.kubot.core.util.AuthToken
import com.example.kubot.core.util.UserId
import com.example.kubot.core.util.Username

interface AuthDao {
    suspend fun getAuthToken(): AuthToken?

    suspend fun setAuthToken(authToken: AuthToken?)

    suspend fun getAuthUsername(): Username?

    suspend fun setAuthUsername(username: Username?)

    suspend fun getAuthUserId(): UserId?

    suspend fun setAuthUserId(userId: UserId?)

    suspend fun clearAuthToken()

    suspend fun getAuthInfo(): AuthInfo?

    suspend fun setAuthInfo(authInfo: AuthInfo?)

    suspend fun clearAuthInfo()
}