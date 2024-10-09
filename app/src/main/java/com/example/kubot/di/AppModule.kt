package com.example.kubot.di

import android.content.Context
import androidx.datastore.core.DataStore
import com.example.kubot.auth_feature.data.AuthApi
import com.example.kubot.auth_feature.data.repository.authRepositoryImpl.AuthRepositoryImpl
import com.example.kubot.auth_feature.data.repository.local.AuthDao
import com.example.kubot.auth_feature.data.repository.local.authDaoImpls.AuthDaoImpl
import com.example.kubot.auth_feature.data.repository.remote.authApiImpls.AuthApiImpl
import com.example.kubot.auth_feature.domain.AuthRepository
import com.example.kubot.core.data.remote.KubotApi
import com.example.kubot.core.data.settings.AppSettings
import com.example.kubot.core.data.settings.AppSettingsRepositoryImpl
import com.example.kubot.core.domain.IAppSettingsRepository
import com.example.kubot.auth_feature.domain.validation.ValidateEmail
import com.example.kubot.auth_feature.domain.validation.ValidatePassword
import com.example.kubot.auth_feature.domain.validation.ValidateUsername
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Singleton

const val USE_FAKE_REPOSITORY = false

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // APP SETTINGS REPOSITORY /////////

    @Provides
    @Singleton
    fun provideAppSettingsRepository(
        dataStore: DataStore<AppSettings>
    ): IAppSettingsRepository = AppSettingsRepositoryImpl(dataStore)


    ////////// AUTHENTICATION REPOSITORY //////////

    @Provides
    @Singleton
    fun provideValidateEmail(): ValidateEmail = ValidateEmail()

    @Provides
    @Singleton
    fun provideValidatePassword(): ValidatePassword = ValidatePassword()

    @Provides
    @Singleton
    fun provideValidateUsername(): ValidateUsername = ValidateUsername()

    @Provides
    @Singleton
    // Hilt chooses this one as the `default` implementation because its not annotated with @Named
    fun provideAuthRepository(
        @AuthApiProdUsingBinds authApiProd: AuthApi,


        @AuthDaoProdUsingBinds authDaoProd: AuthDao,

        validateEmail: ValidateEmail,
        validatePassword: ValidatePassword,
        validateUsername: ValidateUsername,
    ): AuthRepository {
        // This function calls the `provideAuthRepositoryXXXX` functions above,
        //   depending on if we are using the `Fake` or `Prod` implementation.
            // Since we are using the @annotated parameters, these values will be passed
            //   into the provideAuthRepositoryProd function. It will override the @annotations
            //   in the function signature and use these values instead.
            return provideAuthRepositoryProd(
                authApi = authApiProd,
                authDao = authDaoProd,
                validateUsername = validateUsername,
                validatePassword = validatePassword,
                validateEmail = validateEmail
            )
        }


    @Provides
    @Singleton
    @AuthRepositoryProdUsingProvides
    fun provideAuthRepositoryProd(
        @AuthApiProdUsingBinds authApi: AuthApi, // if authApi is not passed in, it will use the @annotated implementation
        @AuthDaoProdUsingBinds authDao: AuthDao, // if authDao is not passed in, it will use the @annotated implementation
        validateUsername: ValidateUsername,
        validateEmail: ValidateEmail,
        validatePassword: ValidatePassword
    ): AuthRepository =
        AuthRepositoryImpl(
            authApi = authApi,
            authDao = authDao,
            validateUsername = validateUsername,
            validateEmail = validateEmail,
            validatePassword = validatePassword
        )

    //////////////////////////////////////
    /// Unused but left here for reference

    @Provides
    @Singleton
    @AuthDaoProdUsingProvides
    fun provideAuthDaoProd(
        @ApplicationContext context: Context,
        appSettingsRepository: IAppSettingsRepository
    ): AuthDao {
        return AuthDaoImpl(
            context,
            appSettingsRepository
        )
    }

    @Provides
    @Singleton
    @AuthApiProdUsingProvides
    fun provideAuthApiProd(
        kubotApi: KubotApi
    ): AuthApi = AuthApiImpl(kubotApi)

    ////////// AGENDA REPOSITORY //////////


    }


@Qualifier
@Named("AuthDao.FAKE.usingBinds")
annotation class AuthDaoFakeUsingBinds

@Qualifier
@Named("AuthDao.PROD.usingBinds")
annotation class AuthDaoProdUsingBinds

@Qualifier
@Named("AuthDao.FAKE.usingProvides")
annotation class AuthDaoFakeUsingProvides

@Qualifier
@Named("AuthDao.PROD.usingProvides")
annotation class AuthDaoProdUsingProvides

@Qualifier
@Named("AuthApi.FAKE.usingBinds")
annotation class AuthApiFakeUsingBinds

@Qualifier
@Named("AuthApi.PROD.usingBinds")
annotation class AuthApiProdUsingBinds

@Qualifier
@Named("AuthApi.FAKE.usingProvides")
annotation class AuthApiFakeUsingProvides

@Qualifier
@Named("AuthApi.PROD.usingProvides")
annotation class AuthApiProdUsingProvides

@Qualifier
@Named("AuthRepository.FAKE.usingProvides")
annotation class AuthRepositoryFakeUsingProvides

@Qualifier
@Named("AuthRepository.PROD.usingProvides")
annotation class AuthRepositoryProdUsingProvides

@Qualifier
@Named("AuthRepository.FAKE.usingBinds")
annotation class AuthRepositoryFakeUsingBinds

@Qualifier
@Named("AuthRepository.PROD.usingBinds")
annotation class AuthRepositoryProdUsingBinds
