package com.neonflip.data.remote.api

import com.neonflip.data.remote.dto.ScoreDto
import com.neonflip.data.remote.dto.SubmitScoreRequestDto
import com.neonflip.data.remote.dto.SubmitScoreResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ScoreApiService {
    @POST("scores/submit")
    suspend fun submitScore(@Body request: SubmitScoreRequestDto): Response<SubmitScoreResponseDto>

    @GET("scores/leaderboard")
    suspend fun getLeaderboard(): Response<List<ScoreDto>>
}
