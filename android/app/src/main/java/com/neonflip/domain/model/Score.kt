package com.neonflip.domain.model

data class Score(
    val id: String,
    val userId: String,
    val username: String,
    val score: Int,
    val createdAt: String
)
