package com.neonflip.domain.model

data class AuthResponse(
    val user: User,
    val accessToken: String,
    val refreshToken: String
)
