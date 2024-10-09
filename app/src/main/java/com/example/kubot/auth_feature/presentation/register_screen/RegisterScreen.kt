package com.example.kubot.auth_feature.presentation.register_screen

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalMinimumTouchTargetEnforcement
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.kubot.R
import com.example.kubot.auth_feature.presentation.components.EmailField
import com.example.kubot.auth_feature.presentation.components.NameField
import com.example.kubot.auth_feature.presentation.components.PasswordField
import com.example.kubot.core.common.modifiers.DP
import com.example.kubot.core.common.modifiers.extraSmallHeight
import com.example.kubot.core.common.modifiers.kubotWideButton
import com.example.kubot.core.common.modifiers.largeHeight
import com.example.kubot.core.common.modifiers.mediumHeight
import com.example.kubot.core.common.modifiers.smallHeight
import com.example.kubot.core.common.modifiers.kubotScreenTopCorners
import com.example.kubot.core.presentation.util.Screens
import com.example.kubot.core.presentation.util.keyboardVisibilityObserver
import com.example.kubot.core.util.InternetConnectivityObserver.InternetAvailabilityIndicator
import com.example.kubot.core.util.InternetConnectivityObserver.InternetConnectivityObserver
import com.example.kubot.ui.theme.KubotShapes
import com.example.kubot.ui.theme.KubotTheme


@Composable
fun RegisterScreen(
    @Suppress("UNUSED_PARAMETER")  // extracted from navArgs in the viewModel
    username: String? = null,
    @Suppress("UNUSED_PARAMETER")  // extracted from navArgs in the viewModel
    email: String? = null,
    @Suppress("UNUSED_PARAMETER")  // extracted from navArgs in the viewModel
    password: String? = null,
    @Suppress("UNUSED_PARAMETER")  // extracted from navArgs in the viewModel
    confirmPassword: String? = null,
    navigator: NavHostController,
    viewModel: RegisterViewModel = hiltViewModel(),
) {
    val registerState by viewModel.registerState.collectAsState()
    val connectivityState by viewModel.onlineState.collectAsState(
        initial = InternetConnectivityObserver.OnlineStatus.OFFLINE // must start as Offline
    )

    RegisterScreenContent(
        state = registerState,
        onAction = viewModel::sendEvent,
        navigator = navigator,
    )

    InternetAvailabilityIndicator(connectivityState)
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RegisterScreenContent(
    state: RegisterState,
    onAction: (RegisterEvent) -> Unit,
    navigator: NavHostController,
) {
    val focusManager = LocalFocusManager.current
    val isKeyboardOpen by keyboardVisibilityObserver()

    fun performRegister() {
        onAction(
            RegisterEvent.Register(
                username = state.username,
                email = state.email,
                password = state.password,
                confirmPassword = state.confirmPassword,
            )
        )

        focusManager.clearFocus()
    }

    fun navigateToLogin() {
        navigator.navigate(
            Screens.Login.route
        )
    }

    BackHandler(true) {
        navigateToLogin()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.onSurface)
    ) col1@{
        Spacer(modifier = Modifier.largeHeight())
        Text(
            text = stringResource(R.string.register_title),
            style = MaterialTheme.typography.h2,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.surface,
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.mediumHeight())

        Column(
            modifier = Modifier
                .kubotScreenTopCorners(color = MaterialTheme.colors.surface)
                .verticalScroll(rememberScrollState())
                .weight(1f)
        ) col2@{

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = DP.small, end = DP.small)
            ) colInnerScroll@{

                Spacer(modifier = Modifier.mediumHeight())

                // • USERNAME
                NameField(
                    value = state.username,
                    label = null,
                    isError = state.isInvalidUsername,
                    onValueChange = {
                        onAction(RegisterEvent.UpdateUsername(it))
                    }
                )
                AnimatedVisibility(state.isInvalidUsername && state.isInvalidUsernameMessageVisible) {
                    Text(text = stringResource(R.string.error_invalid_username), color = Color.Red)
                }
                Spacer(modifier = Modifier.smallHeight())

                // • EMAIL
                EmailField(
                    value = state.email,
                    label = null,
                    isError = state.isInvalidEmail,
                    onValueChange = {
                        onAction(RegisterEvent.UpdateEmail(it))
                    }
                )
                AnimatedVisibility(state.isInvalidEmail && state.isInvalidEmailMessageVisible) {
                    Text(text = stringResource(R.string.error_invalid_email), color = Color.Red)
                }
                Spacer(modifier = Modifier.smallHeight())

                // • PASSWORD
                PasswordField(
                    value = state.password,
                    label = null,
                    isError = state.isInvalidPassword,
                    onValueChange = {
                        onAction(RegisterEvent.UpdatePassword(it))
                    },
                    isPasswordVisible = state.isPasswordVisible,
                    clickTogglePasswordVisibility = {
                        onAction(RegisterEvent.SetIsPasswordVisible(!state.isPasswordVisible))
                    },
                    imeAction = ImeAction.Next,
                )
                if (state.isInvalidPassword && state.isInvalidPasswordMessageVisible) {
                    Text(text = stringResource(R.string.error_invalid_password), color = Color.Red)
                }
                Spacer(modifier = Modifier.smallHeight())

                // • CONFIRM PASSWORD
                PasswordField(
                    label = null, //stringResource(R.string.register_label_confirm_password),
                    placeholder = stringResource(R.string.register_placeholder_confirm_password),
                    value = state.confirmPassword,
                    isError = state.isInvalidConfirmPassword,
                    onValueChange = {
                        onAction(RegisterEvent.UpdateConfirmPassword(it))
                    },
                    isPasswordVisible = state.isPasswordVisible,
                    clickTogglePasswordVisibility = {
                        onAction(RegisterEvent.SetIsPasswordVisible(!state.isPasswordVisible))
                    },
                    imeAction = ImeAction.Done,
                    doneAction = {
                        performRegister()
                    },
                )
                AnimatedVisibility(state.isInvalidConfirmPassword && state.isInvalidConfirmPasswordMessageVisible) {
                    Text(
                        text = stringResource(R.string.error_invalid_confirm_password),
                        color = Color.Red
                    )
                    Spacer(modifier = Modifier.extraSmallHeight())
                }
                // • SHOW IF MATCHING PASSWORDS
                AnimatedVisibility(!state.isPasswordsMatch) {
                    Text(
                        text = stringResource(R.string.register_error_passwords_do_not_match),
                        color = Color.Red
                    )
                    Spacer(modifier = Modifier.extraSmallHeight())
                }
                // • SHOW PASSWORD REQUIREMENTS
                AnimatedVisibility(state.isInvalidPasswordMessageVisible || state.isInvalidConfirmPasswordMessageVisible) {
                    Text(
                        text = stringResource(R.string.register_password_requirements),
                        color = Color.Red
                    )
                    Spacer(modifier = Modifier.extraSmallHeight())
                }
                Spacer(modifier = Modifier.mediumHeight())

                // • REGISTER BUTTON
                Button(
                    onClick = {
                        performRegister()
                    },
                    enabled = !state.isLoading,
                    modifier = Modifier
                        .kubotWideButton(color = MaterialTheme.colors.primary)
                        .align(alignment = Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = stringResource(R.string.register_button),
                        fontSize = MaterialTheme.typography.button.fontSize,
                    )
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(start = DP.small)
                                .size(16.dp)
                                .align(alignment = Alignment.CenterVertically)
                        )
                    }
                }
                Spacer(modifier = Modifier.mediumHeight())

                // STATUS //////////////////////////////////////////

                AnimatedVisibility(state.errorMessage != null) {
                    state.errorMessage?.getOrNull?.let { errorMessage ->
                        Spacer(modifier = Modifier.extraSmallHeight())
                        Text(
                            text = errorMessage,
                            color = Color.Red,
                            modifier = Modifier
                                .animateContentSize()
                        )
                        Spacer(modifier = Modifier.extraSmallHeight())
                    }
                }
                AnimatedVisibility(state.statusMessage != null) {
                    state.statusMessage?.getOrNull?.let { message ->
                        Spacer(modifier = Modifier.extraSmallHeight())
                        Text(text = message)
                        Spacer(modifier = Modifier.extraSmallHeight())
                    }
                }
            }


            // • GO TO LOGIN BUTTON
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = DP.small, bottom = DP.small)
                    .weight(1f)
            ) {
                this@col1.AnimatedVisibility(
                    visible = !isKeyboardOpen,
                    enter = fadeIn() + slideInVertically(
                        initialOffsetY = { it }
                    ),
                    exit = fadeOut(),
                    modifier = Modifier
                        .background(color = MaterialTheme.colors.surface)
                        .align(alignment = Alignment.BottomStart)
                ) {
                    // • BACK TO LOGIN BUTTON
                    CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) { // allows smaller touch-targets
                        IconButton(
                            onClick = {
                                navigateToLogin()
                            },
                            modifier = Modifier
                                .size(DP.XXLarge)
                                .clip(shape = KubotShapes.MediumButtonRoundedCorners)
                                .background(color = MaterialTheme.colors.onSurface)
                                .align(alignment = Alignment.BottomStart)
                        ) {
                            Icon(
                                tint = MaterialTheme.colors.surface,
                                imageVector = Icons.Filled.ChevronLeft,
                                contentDescription = stringResource(R.string.register_description_back),
                                modifier = Modifier
                                    .align(alignment = Alignment.Center)
                                    .size(DP.XXLarge)
                                    .padding(
                                        start = 9.dp,
                                        end = 9.dp
                                    ) // fine tunes the icon size (weird)
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    group = "Night Mode=true"
)
fun RegisterScreenPreview() {
    KubotTheme {
        Surface {
            RegisterScreenContent(
                navigator = rememberNavController(),
                state = RegisterState(
                    email = "arfin@demo.com",
                    password = "1234567Az",
                    confirmPassword = "1234567Az",
                    isInvalidEmail = false,
                    isInvalidPassword = false,
                    isInvalidConfirmPassword = false,
                    isPasswordsMatch = true,
                    isInvalidEmailMessageVisible = false,
                    isInvalidPasswordMessageVisible = false,
                    isInvalidConfirmPasswordMessageVisible = false,
                    isPasswordVisible = false,
                    isLoading = false,
                    errorMessage = null,  // UiText.Res(R.string.error_invalid_email),
                    statusMessage = null, // UiText.Res(R.string.login_logged_in),
                    authInfo = null,
                ),
                onAction = {},
            )
        }
    }
}

@Composable
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    group = "Night Mode=false"
)
fun RegisterScreenPreview_NightMode_NO() {
    RegisterScreenPreview()
}
