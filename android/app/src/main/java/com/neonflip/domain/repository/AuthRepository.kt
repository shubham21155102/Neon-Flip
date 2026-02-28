package com.neonflip.domain.repository

import com.neonflip.domain.model.AuthRequest
import com.neonflip.domain.model.AuthResponse
import com.neonflip.domain.model.Result
import com.neonflip.domain.model.User

interface AuthRepository {
    suspend fun login(request: AuthRequest.Login): Result<AuthResponse>
    suspend fun register(request: AuthRequest.Register): Result<AuthResponse>
    suspend fun getCurrentUser(): Result<User>
    suspend fun logout()
    suspend fun isLoggedIn(): Boolean
}
