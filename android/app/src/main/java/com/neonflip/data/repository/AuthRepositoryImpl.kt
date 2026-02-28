package com.neonflip.data.repository

import com.neonflip.data.local.TokenStorage
import com.neonflip.data.mapper.toDomain
import com.neonflip.data.remote.api.AuthApiService
import com.neonflip.data.remote.dto.LoginRequestDto
import com.neonflip.data.remote.dto.RegisterRequestDto
import com.neonflip.domain.model.ApiError
import com.neonflip.domain.model.AuthRequest
import com.neonflip.domain.model.AuthResponse
import com.neonflip.domain.model.Result
import com.neonflip.domain.model.User
import com.neonflip.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApiService: AuthApiService,
    private val tokenStorage: TokenStorage
) : AuthRepository {

    override suspend fun login(request: AuthRequest.Login): Result<AuthResponse> {
        return try {
            val response = authApiService.login(
                LoginRequestDto(
                    email = request.email,
                    password = request.password
                )
            )

            if (response.isSuccessful && response.body() != null) {
                val authResponseDto = response.body()!!
                tokenStorage.saveTokens(
                    accessToken = authResponseDto.accessToken,
                    refreshToken = authResponseDto.refreshToken
                )
                Result.Success(
                    AuthResponse(
                        user = authResponseDto.user.toDomain(),
                        accessToken = authResponseDto.accessToken,
                        refreshToken = authResponseDto.refreshToken
                    )
                )
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = if (errorBody != null) {
                    try {
                        com.google.gson.Gson().fromJson(
                            errorBody,
                            com.neonflip.data.remote.dto.ErrorResponseDto::class.java
                        ).message
                    } catch (e: Exception) {
                        "Login failed"
                    }
                } else {
                    "Login failed"
                }
                Result.Error(ApiError(message = errorMessage, statusCode = response.code()))
            }
        } catch (e: Exception) {
            Result.Error(ApiError(message = e.message ?: "Network error", statusCode = 0))
        }
    }

    override suspend fun register(request: AuthRequest.Register): Result<AuthResponse> {
        return try {
            val response = authApiService.register(
                RegisterRequestDto(
                    username = request.username,
                    email = request.email,
                    password = request.password
                )
            )

            if (response.isSuccessful && response.body() != null) {
                val authResponseDto = response.body()!!
                tokenStorage.saveTokens(
                    accessToken = authResponseDto.accessToken,
                    refreshToken = authResponseDto.refreshToken
                )
                Result.Success(
                    AuthResponse(
                        user = authResponseDto.user.toDomain(),
                        accessToken = authResponseDto.accessToken,
                        refreshToken = authResponseDto.refreshToken
                    )
                )
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = if (errorBody != null) {
                    try {
                        com.google.gson.Gson().fromJson(
                            errorBody,
                            com.neonflip.data.remote.dto.ErrorResponseDto::class.java
                        ).message
                    } catch (e: Exception) {
                        "Registration failed"
                    }
                } else {
                    "Registration failed"
                }
                Result.Error(ApiError(message = errorMessage, statusCode = response.code()))
            }
        } catch (e: Exception) {
            Result.Error(ApiError(message = e.message ?: "Network error", statusCode = 0))
        }
    }

    override suspend fun getCurrentUser(): Result<User> {
        return try {
            val response = authApiService.getCurrentUser()

            if (response.isSuccessful && response.body() != null) {
                Result.Success(response.body()!!.toDomain())
            } else {
                Result.Error(ApiError(message = "Failed to get user", statusCode = response.code()))
            }
        } catch (e: Exception) {
            Result.Error(ApiError(message = e.message ?: "Network error", statusCode = 0))
        }
    }

    override suspend fun logout() {
        tokenStorage.clearTokens()
    }

    override suspend fun isLoggedIn(): Boolean {
        return tokenStorage.hasValidToken()
    }
}
