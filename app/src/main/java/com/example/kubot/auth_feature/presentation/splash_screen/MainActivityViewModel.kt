package com.example.kubot.auth_feature.presentation.splash_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kubot.auth_feature.domain.AuthInfo
import com.example.kubot.auth_feature.domain.AuthRepository
import com.example.kubot.core.domain.IAppSettingsRepository
import com.example.kubot.core.util.Exceptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    val appSettingsRepository: IAppSettingsRepository
) : ViewModel() {

    private val _splashState = MutableStateFlow(SplashState())
    val splashState = _splashState.asStateFlow()


    fun onSetAuthInfo(authInfo: AuthInfo?) {
        viewModelScope.launch {

            // set the AuthInfo (& AuthToken) for this user from the AuthRepository
            authRepository.setAuthInfo(authInfo)

            // Validate the AuthToken
            val authenticateSuccess = try {
                authRepository.authenticate()
                true
            } catch (e: Exceptions.NetworkException) {
                if (e.localizedMessage == "401 Unauthorized") {
                    false
                } else {
                    authInfo?.token != null
                }
            } catch (e: Exceptions.UnknownErrorException) {
                authInfo?.token != null
            } catch (e: Exception) {
                _splashState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
                false
            }

            if (authenticateSuccess) {
                // User is authenticated
                _splashState.update {
                    it.copy(
                        authInfo = authInfo,
                        isLoading = false,
                    )
                }
            } else {
                // User is not authenticated
                _splashState.update {
                    it.copy(
                        authInfo = null,
                        isLoading = false,
                    )
                }
            }
        }
    }
}
