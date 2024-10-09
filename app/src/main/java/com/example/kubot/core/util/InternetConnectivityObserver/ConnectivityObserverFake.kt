package com.example.kubot.core.util.InternetConnectivityObserver

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class ConnectivityObserverFake : InternetConnectivityObserver {
    override val onlineStateFlow =
        flow<InternetConnectivityObserver.OnlineStatus> {
            emit(InternetConnectivityObserver.OnlineStatus.ONLINE)
        }

    override fun connectivityFlow(): Flow<InternetConnectivityObserver.ConnectivityStatus> {
        return onlineStateFlow.map {
            if (it == InternetConnectivityObserver.OnlineStatus.ONLINE)
                InternetConnectivityObserver.ConnectivityStatus.Available
            else
                InternetConnectivityObserver.ConnectivityStatus.Unavailable
        }
    }
}
