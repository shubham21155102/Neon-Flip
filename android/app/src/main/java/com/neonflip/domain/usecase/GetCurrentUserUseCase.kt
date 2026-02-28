package com.neonflip.domain.usecase

import com.neonflip.domain.model.Result
import com.neonflip.domain.model.User
import com.neonflip.domain.repository.AuthRepository
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<User> {
        return authRepository.getCurrentUser()
    }
}
