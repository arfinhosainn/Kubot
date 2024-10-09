package com.example.kubot.auth_feature.domain

import android.os.Parcelable
import com.example.kubot.core.util.AuthToken
import com.example.kubot.core.util.Email
import com.example.kubot.core.util.UserId
import com.example.kubot.core.util.Username
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable


@Parcelize
@Serializable
data class AuthInfo(
    val token: String? = null,
    val id: String? = null,
    val name: String? = null,
    val emailAddress: String? = null,
): Parcelable