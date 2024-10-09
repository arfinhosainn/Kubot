package com.example.kubot.core.data.remote

import com.example.kubot.auth_feature.data.AuthApi.Companion.createBearerTokenString
import com.example.kubot.auth_feature.data.repository.remote.DTOs.auth.ApiCredentialsDTO
import com.example.kubot.auth_feature.data.repository.remote.DTOs.auth.AuthInfoDTO
import com.example.kubot.core.util.AuthToken

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface KubotApi {

    @GET("/token_verification")
    suspend fun authenticateAuthToken(
        authToken: AuthToken?,
        @Header("Authorization") authorizationHeader: String = createBearerTokenString(authToken),
    ): Response<Void>


    @GET("authenticate")
    suspend fun authenticate(
        // Uses the Authorization Header created in the the interceptor
    ): Response<Void>


    @POST("/api/v1/auth/sign_in")
    suspend fun login(
        @Body credentials: ApiCredentialsDTO
    ): Response<AuthInfoDTO>


    @POST("/api/v1/auth/sign_up")
    suspend fun register(
        @Body credentials: ApiCredentialsDTO
    ): Response<Void>


    @GET("/sign_out")
    suspend fun logout(): Response<Void>

}