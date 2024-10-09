package com.example.kubot.auth_feature.data.repository.authRepositoryImpl

import com.example.kubot.auth_feature.data.AuthApi
import com.example.kubot.auth_feature.data.repository.local.AuthDao
import com.example.kubot.auth_feature.data.repository.remote.DTOs.auth.AuthInfoDTO
import com.example.kubot.auth_feature.domain.AuthInfo
import com.example.kubot.auth_feature.domain.AuthRepository
import com.example.kubot.core.util.Email
import com.example.kubot.core.util.Exceptions
import com.example.kubot.core.util.Password
import com.example.kubot.core.util.Username
import com.example.kubot.auth_feature.data.convertersDTOEntityDomain.toDomain
import com.example.kubot.auth_feature.domain.validation.ValidateEmail
import com.example.kubot.auth_feature.domain.validation.ValidatePassword
import com.example.kubot.auth_feature.domain.validation.ValidateUsername
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val authDao: AuthDao,
    override val validateEmail: ValidateEmail,
    override val validatePassword: ValidatePassword,
    override val validateUsername: ValidateUsername,

    ) : AuthRepository {

    override suspend fun login(
        email: Email,
        password: Password
    ): AuthInfo {
        // Sanity check to be sure the email and password are valid
        if (!validateEmail.validate(email)) {
            throw Exceptions.InvalidEmailException()
        }
        if (!validatePassword.validate(password)) {
            throw Exceptions.InvalidPasswordException()
        }

        val authInfoDTO: AuthInfoDTO? = try {
            authApi.login(email.trim(), password)
        } catch (e: Exceptions.LoginException) {
            throw e
        } catch (e: Exceptions.NetworkException) {
            throw e
        } catch (e: Exceptions.WrongPasswordException) {
            throw e
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            throw Exceptions.UnknownErrorException(e.message)
        }

        val authInfo =
            authInfoDTO?.copy(emailAddress = email).toDomain() // include email in the AuthInfo

        authInfo?.let {
            authDao.setAuthInfo(authInfo)
            authApi.setAuthUserId(authInfo.id)
            return authDao.getAuthInfo()
                ?: throw Exceptions.LoginException("No AuthInfo")
        } ?: throw Exceptions.LoginException("No AuthInfo")
    }

    override suspend fun register(
        username: Username,
        email: Email,
        password: Password
    ) {
        // Sanity check to be sure the username, email, and password are valid
        if (!validateUsername.validate(username)) {
            throw Exceptions.InvalidUsernameException()
        }
        if (!validateEmail.validate(email)) {
            throw Exceptions.InvalidEmailException()
        }
        if (!validatePassword.validate(password)) {
            throw Exceptions.InvalidPasswordException()
        }

        try {
            authApi.register(username.trim(), email.trim(), password)
        } catch (e: Exceptions.RegisterException) {
            throw e
        } catch (e: Exceptions.NetworkException) {
            throw e
        } catch (e: Exceptions.EmailAlreadyExistsException) {
            throw e
        } catch (e: Exceptions.UnknownErrorException) {
            throw e
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            throw Exceptions.UnknownErrorException(e.message)
        }
    }

    override suspend fun logout() {
        try {
            authDao.clearAuthInfo()
            authApi.logout()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun setAuthInfo(authInfo: AuthInfo?) {
        authApi.setAuthToken(authInfo?.token)
        authApi.setAuthUserId(authInfo?.id)

        authDao.setAuthInfo(authInfo)
    }

    override suspend fun getAuthInfo(): AuthInfo? {
        return authDao.getAuthInfo()
    }

    override suspend fun clearAuthInfo() {
        authApi.clearAuthToken()
        authDao.clearAuthInfo()
    }


    override fun getAuthUserId(): String? {
        TODO("Not yet implemented")
    }

    override suspend fun authenticate(): Boolean {
        return try {
            authApi.authenticate()
        } catch (e: Exceptions.NetworkException) {
            throw e
        } catch (e: Exceptions.UnknownErrorException) {
            throw e
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            throw Exceptions.UnknownErrorException(e.message)
        }
    }

    override suspend fun authenticateAuthInfo(authInfo: AuthInfo?): Boolean {
        if (authInfo == null) return false
        if (authInfo.token.isNullOrBlank()) return false

        return try {
            authApi.authenticateAuthToken(authInfo.token)
        } catch (e: CancellationException) {
            false
        } catch (e: Exception) {
            false
        }
    }
}