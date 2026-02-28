package com.neonflip.data.mapper

import com.neonflip.data.remote.dto.ScoreDto
import com.neonflip.domain.model.Score

fun ScoreDto.toDomain(): Score {
    return Score(
        id = id,
        userId = userId,
        username = username,
        score = score,
        createdAt = createdAt
    )
}
