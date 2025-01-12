package com.example.kubot.core.util.InternetConnectivityObserver

import kotlinx.coroutines.flow.Flow

interface InternetConnectivityObserver {

    val onlineStateFlow: Flow<OnlineStatus>

    fun connectivityFlow(): Flow<ConnectivityStatus>

    enum class OnlineStatus {
        ONLINE,
        OFFLINE
    }

    enum class InternetReachabilityStatus {
        REACHABLE,
        UNREACHABLE,
    }

    enum class ConnectivityStatus {
        Available, Unavailable, Losing, Lost
    }
}