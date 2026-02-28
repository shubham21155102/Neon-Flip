package com.neonflip.domain.usecase

import com.neonflip.domain.model.Result
import com.neonflip.domain.model.Score
import com.neonflip.domain.repository.ScoreRepository
import javax.inject.Inject

class SubmitScoreUseCase @Inject constructor(
    private val scoreRepository: ScoreRepository
) {
    suspend operator fun invoke(score: Int): Result<Score> {
        if (score < 0) {
            return Result.Error(
                com.neonflip.domain.model.ApiError(
                    message = "Score cannot be negative",
                    statusCode = 400
                )
            )
        }
        return scoreRepository.submitScore(score)
    }
}
