package com.example.kubot.auth_feature.data.repository.remote.DTOs.auth

import kotlinx.serialization.Serializable

@Serializable
data class ApiCredentialsDTO (
    val emailAddress: String? = null,
    val password: String? = null,
    val id: String? = null,
    val name: String? = null,
    val lastName: String? = null,
    val createdAt: String?= null,
    val updatedAt: String?= null,
    val phone: String? = null,
)