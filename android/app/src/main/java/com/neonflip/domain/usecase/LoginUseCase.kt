package com.neonflip.domain.usecase

import com.neonflip.domain.model.AuthRequest
import com.neonflip.domain.model.AuthResponse
import com.neonflip.domain.model.Result
import com.neonflip.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<AuthResponse> {
        if (email.isBlank() || password.isBlank()) {
            return Result.Error(
                com.neonflip.domain.model.ApiError(
                    message = "Email and password are required",
                    statusCode = 400
                )
            )
        }
        return authRepository.login(AuthRequest.Login(email, password))
    }
}
