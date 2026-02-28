package com.neonflip.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ErrorResponseDto(
    @SerializedName("message")
    val message: String,
    @SerializedName("error")
    val error: String? = null,
    @SerializedName("statusCode")
    val statusCode: Int
)
