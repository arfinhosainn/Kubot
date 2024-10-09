package com.example.kubot.auth_feature.data.repository.remote.DTOs.auth

import kotlinx.serialization.Serializable


@Serializable
data class AuthInfoDTO(
    val token: String? = null,
    val id: String? = null,
    val name: String? = null,
    val emailAddress: String? = null,
)