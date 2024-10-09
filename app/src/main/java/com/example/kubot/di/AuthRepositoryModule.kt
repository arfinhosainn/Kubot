package com.example.kubot.di

import com.example.kubot.auth_feature.data.AuthApi
import com.example.kubot.auth_feature.data.repository.authRepositoryImpl.AuthRepositoryImpl
import com.example.kubot.auth_feature.data.repository.local.AuthDao
import com.example.kubot.auth_feature.data.repository.local.authDaoImpls.AuthDaoImpl
import com.example.kubot.auth_feature.data.repository.remote.authApiImpls.AuthApiImpl
import com.example.kubot.auth_feature.domain.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthRepositoryModule {



    @Binds
    @Singleton
    @AuthDaoProdUsingBinds
    abstract fun bindAuthDaoProd(
        authDaoProdImpl: AuthDaoImpl  // <-- provides this instance...
    ): AuthDao // <-- ... for this interface.


    @Binds
    @Singleton
    @AuthApiProdUsingBinds
    abstract fun bindAuthApiProd(
        authApiProdImpl: AuthApiImpl // <-- provides this instance...
    ): AuthApi // <-- ... for this interface.


    @Binds
    @Singleton
    @AuthRepositoryProdUsingBinds
    abstract fun bindAuthRepositoryProd(
        authRepositoryProdImpl: AuthRepositoryImpl // <-- provides this instance...
    ): AuthRepository // <-- ... for this interface.
}