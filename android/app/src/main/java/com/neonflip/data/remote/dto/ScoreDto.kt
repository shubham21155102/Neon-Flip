package com.neonflip.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ScoreDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("score")
    val score: Int,
    @SerializedName("created_at")
    val createdAt: String
)

data class SubmitScoreRequestDto(
    @SerializedName("score")
    val score: Int
)

data class SubmitScoreResponseDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("score")
    val score: Int,
    @SerializedName("created_at")
    val createdAt: String
)
