package com.example.kubot.di

import android.content.Context
import com.example.kubot.core.util.InternetConnectivityObserver.InternetConnectivityObserver
import com.example.kubot.core.util.InternetConnectivityObserver.InternetConnectivityObserverImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object InternetConnectivityObserverModule {

    @Provides
    @Singleton
    fun provideInternetConnectivityObserverProd(
        @ApplicationContext context: Context
    ): InternetConnectivityObserver {
        return InternetConnectivityObserverImpl(context)
    }
}