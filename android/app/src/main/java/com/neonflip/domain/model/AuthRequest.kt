package com.neonflip.domain.model

sealed class AuthRequest {
    data class Login(
        val email: String,
        val password: String
    ) : AuthRequest()

    data class Register(
        val username: String,
        val email: String,
        val password: String
    ) : AuthRequest()
}
