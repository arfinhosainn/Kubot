package com.example.kubot.auth_feature.data

import com.example.kubot.auth_feature.data.repository.remote.DTOs.auth.AuthInfoDTO
import com.example.kubot.core.util.AuthToken
import com.example.kubot.core.util.Email
import com.example.kubot.core.util.Password
import com.example.kubot.core.util.UserId
import com.example.kubot.core.util.Username
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import logcat.logcat

interface AuthApi {

    suspend fun login(
        email: Email,
        password: Password
    ): AuthInfoDTO?

    suspend fun register(
        username: Username,
        email: Email,
        password: Password
    )

    suspend fun authenticate(): Boolean // uses the AuthToken stored in this companion object
    suspend fun authenticateAuthToken(authToken: AuthToken?): Boolean

    suspend fun logout(
        // uses the AuthToken stored in this companion object
    )

    fun setAuthToken(authToken: AuthToken?) {

        AuthApi.Companion.authToken = authToken
    }

    fun clearAuthToken() {
        AuthApi.Companion.authToken = null
        AuthApi.Companion.authUserId = null
    }

    fun setAuthUserId(authUserId: UserId?) {
        AuthApi.Companion.authUserId = authUserId
    }

    companion object {

        // 1. Check for a valid AuthToken in the IAuthApi Companion object.
        //   2a. If valid, return it.
        //   2b. If not valid, attempt to fetch it from the authTokenRetriever (usually an AuthDao fun).
        //       Set authToken in Companion object, for faster access.
        fun getAuthToken(authTokenRetriever: (suspend () -> AuthToken?)? = null ): AuthToken? {
            // 2a - if valid, return it
            AuthApi.Companion.authToken?.let { return it }

            // 2b - Attempt to fetch it
            return runBlocking(Dispatchers.IO) {
                logcat { "AuthToken invalid - Attempt Retrieve AuthToken from repo" }
                authTokenRetriever?.run {
                    AuthApi.Companion.authToken = authTokenRetriever()
                }

                AuthApi.Companion.authToken
            }
        }

        fun createBearerTokenString(authToken: String?): String {
            return "Bearer ${authToken ?: "NULL_AUTH_TOKEN"}"
        }

        var authToken: String? = null
        var authUserId: UserId? = null
    }


}