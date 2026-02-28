package com.neonflip.domain.repository

import com.neonflip.domain.model.Result
import com.neonflip.domain.model.Score

interface ScoreRepository {
    suspend fun submitScore(score: Int): Result<Score>
    suspend fun getLeaderboard(): Result<List<Score>>
}
