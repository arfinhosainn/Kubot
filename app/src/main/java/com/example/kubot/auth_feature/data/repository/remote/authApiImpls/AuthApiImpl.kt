package com.example.kubot.auth_feature.data.repository.remote.authApiImpls

import com.example.kubot.auth_feature.data.AuthApi
import com.example.kubot.auth_feature.data.repository.remote.DTOs.auth.ApiCredentialsDTO
import com.example.kubot.auth_feature.data.repository.remote.DTOs.auth.AuthInfoDTO
import com.example.kubot.core.data.remote.KubotApi
import com.example.kubot.core.data.remote.utils.getErrorBodyMessage
import com.example.kubot.core.util.AuthToken
import com.example.kubot.core.util.Email
import com.example.kubot.core.util.Exceptions
import com.example.kubot.core.util.InternetConnectivityObserver.InternetConnectivityObserverImpl.Companion.isInternetReachable
import com.example.kubot.core.util.Password
import com.example.kubot.core.util.Username
import com.example.kubot.core.util.authToken
import com.example.kubot.core.util.userId
import com.example.kubot.core.util.username
import retrofit2.HttpException
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

// Uses real network API & Retrofit calls

class AuthApiImpl @Inject constructor (
    private val kubotApi: KubotApi
): AuthApi {

    override suspend fun login(
        email: Email,
        password: Password
    ): AuthInfoDTO {

        if(!isInternetReachable)
            throw Exceptions.NetworkException("No internet connection")

        try {
            val response =
                kubotApi.login(ApiCredentialsDTO(emailAddress = email, password = password))

            when(response.code()) {
                200 -> {
                    return AuthInfoDTO(
                        authToken(response.body()?.token
                            ?: throw Exceptions.LoginException("No Token")
                        ),
                        userId(response.body()?.id
                            ?: throw Exceptions.LoginException("No User Id")
                        ),
                        username(response.body()?.name
                            ?: throw Exceptions.LoginException("No Full Name")
                    ))
                }
                409 -> throw Exceptions.LoginException(
                    getErrorBodyMessage(response.errorBody()?.string())
                )
                else -> throw Exceptions.UnknownErrorException(
                    "${response.message()} - " +
                    "${response.code()} - " +
                    getErrorBodyMessage(response.errorBody()?.string())
                )
            }
        } catch (e: Exceptions.NetworkException) {
            throw e
        } catch (e: Exceptions.UnknownErrorException) {
            throw e
        } catch (e: Exceptions.LoginException) {
            throw e
        } catch (e: HttpException) {
            throw Exceptions.NetworkException("${e.message()} - ${e.code()}")
        } catch (e: java.net.UnknownHostException) {
            throw Exceptions.NetworkException(e.message)
        } catch(e: CancellationException) {
            throw e
        } catch (e: Exception) {
            throw Exceptions.UnknownErrorException(e.message ?: "Unknown Error")
        }
    }

    override suspend fun register(
        username: Username,
        email: Email,
        password: Password
    ) {
        if(!isInternetReachable)
            throw Exceptions.NetworkException("No internet connection")

        try {
            val response =
                kubotApi.register(
                    ApiCredentialsDTO(
                    name = username,
                    emailAddress = email,
                    password = password
                ))

            when(response.code()) {
                200 -> return // Success
                409 -> throw Exceptions.RegisterException(
                    getErrorBodyMessage(response.errorBody()?.string())
                )
                else -> throw Exceptions.UnknownErrorException(
                    "${response.message()} - " +
                    "${response.code()} - " +
                    getErrorBodyMessage(response.errorBody()?.string())
                )
            }
        } catch (e: Exceptions.NetworkException) {
            throw e
        } catch (e: Exceptions.RegisterException) {
            throw e
        } catch (e: Exceptions.UnknownErrorException) {
            throw e
        } catch (e: HttpException) {
            throw Exceptions.NetworkException("${e.message()} - ${e.code()}")
        } catch (e: java.net.UnknownHostException) {
            throw Exceptions.NetworkException(e.message)
        } catch(e: CancellationException) {
            throw e
        } catch (e: Exception) {
            throw Exceptions.UnknownErrorException(e.message ?: "Unknown Error")
        }
    }

    override suspend fun authenticate(): Boolean {
        if(!isInternetReachable) return false

        try {
            // Use the current user's AuthToken from the IAuthApi companion object
            val response = kubotApi.authenticate()
            
            return when(response.code()) {
                200 -> true // Success
                401 -> false
                429 -> throw Exceptions.NetworkException(
                    "Rate Limit Exceeded - " +
                    "${response.code()} - " +
                    getErrorBodyMessage(response.errorBody()?.string())
                )
                else -> throw Exceptions.UnknownErrorException(
                    "${response.message()} - " +
                    "${response.code()} - " +
                    getErrorBodyMessage(response.errorBody()?.string())
                )
            }
        } catch (e: Exceptions.NetworkException) {
            throw e
        } catch (e: Exceptions.UnknownErrorException) {
            throw e
        } catch (e: HttpException) {
            throw Exceptions.NetworkException("${e.message()} - ${e.code()}")
        } catch (e: java.net.UnknownHostException) {
            throw Exceptions.NetworkException(e.message)
        } catch(e: CancellationException) {
            throw e
        } catch (e: Exception) {
            throw Exceptions.UnknownErrorException(e.message ?: "Unknown Error")
        }
    }

    override suspend fun authenticateAuthToken(authToken: AuthToken?): Boolean {
        authToken ?: return false
        if(!isInternetReachable) return false

        try {
            val response =
                kubotApi.authenticateAuthToken(authToken)

            return when(response.code()) {
                200 -> true // Success
                401 -> false
                else -> throw Exceptions.UnknownErrorException(
                    "${response.message()} - " +
                            "${response.code()} - " +
                            getErrorBodyMessage(response.errorBody()?.string())
                )
            }
        } catch (e: Exceptions.NetworkException) {
            throw e
        } catch (e: Exceptions.UnknownErrorException) {
            throw e
        } catch (e: HttpException) {
            throw Exceptions.NetworkException("${e.message()} - ${e.code()}")
        } catch (e: java.net.UnknownHostException) {
            throw Exceptions.NetworkException(e.message)
        } catch(e: CancellationException) {
            throw e
        } catch (e: Exception) {
            throw Exceptions.UnknownErrorException(e.message ?: "Unknown Error")
        }
    }

    override suspend fun logout() {
        if(!isInternetReachable) return

        try {
            kubotApi.logout()
        } catch (e: Exceptions.NetworkException) {
            throw e
        } catch (e: Exceptions.UnknownErrorException) {
            throw e
        } catch (e: HttpException) {
            throw Exceptions.NetworkException("${e.message()} - ${e.code()}")
        } catch (e: java.net.UnknownHostException) {
            throw Exceptions.NetworkException(e.message)
        } catch(e: CancellationException) {
            throw e
        } catch (e: Exception) {
            throw Exceptions.UnknownErrorException(e.message ?: "Unknown Error")
        }

    }
}