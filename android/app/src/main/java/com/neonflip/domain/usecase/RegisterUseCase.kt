package com.neonflip.domain.usecase

import com.neonflip.domain.model.AuthRequest
import com.neonflip.domain.model.AuthResponse
import com.neonflip.domain.model.Result
import com.neonflip.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(username: String, email: String, password: String): Result<AuthResponse> {
        if (username.isBlank() || email.isBlank() || password.isBlank()) {
            return Result.Error(
                com.neonflip.domain.model.ApiError(
                    message = "Username, email and password are required",
                    statusCode = 400
                )
            )
        }
        if (password.length < 6) {
            return Result.Error(
                com.neonflip.domain.model.ApiError(
                    message = "Password must be at least 6 characters",
                    statusCode = 400
                )
            )
        }
        return authRepository.register(AuthRequest.Register(username, email, password))
    }
}
