package com.neonflip.domain.usecase

import com.neonflip.domain.model.Result
import com.neonflip.domain.model.Score
import com.neonflip.domain.repository.ScoreRepository
import javax.inject.Inject

class GetLeaderboardUseCase @Inject constructor(
    private val scoreRepository: ScoreRepository
) {
    suspend operator fun invoke(): Result<List<Score>> {
        return scoreRepository.getLeaderboard()
    }
}
