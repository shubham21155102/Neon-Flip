package com.neonflip.data.repository

import com.neonflip.data.mapper.toDomain
import com.neonflip.data.remote.api.ScoreApiService
import com.neonflip.data.remote.dto.SubmitScoreRequestDto
import com.neonflip.domain.model.ApiError
import com.neonflip.domain.model.Result
import com.neonflip.domain.model.Score
import com.neonflip.domain.repository.ScoreRepository
import javax.inject.Inject

class ScoreRepositoryImpl @Inject constructor(
    private val scoreApiService: ScoreApiService
) : ScoreRepository {

    override suspend fun submitScore(score: Int): Result<Score> {
        return try {
            val response = scoreApiService.submitScore(SubmitScoreRequestDto(score = score))

            if (response.isSuccessful && response.body() != null) {
                Result.Success(response.body()!!.toDomain())
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = if (errorBody != null) {
                    try {
                        com.google.gson.Gson().fromJson(
                            errorBody,
                            com.neonflip.data.remote.dto.ErrorResponseDto::class.java
                        ).message
                    } catch (e: Exception) {
                        "Failed to submit score"
                    }
                } else {
                    "Failed to submit score"
                }
                Result.Error(ApiError(message = errorMessage, statusCode = response.code()))
            }
        } catch (e: Exception) {
            Result.Error(ApiError(message = e.message ?: "Network error", statusCode = 0))
        }
    }

    override suspend fun getLeaderboard(): Result<List<Score>> {
        return try {
            val response = scoreApiService.getLeaderboard()

            if (response.isSuccessful && response.body() != null) {
                Result.Success(response.body()!!.map { it.toDomain() })
            } else {
                Result.Error(ApiError(message = "Failed to get leaderboard", statusCode = response.code()))
            }
        } catch (e: Exception) {
            Result.Error(ApiError(message = e.message ?: "Network error", statusCode = 0))
        }
    }
}
