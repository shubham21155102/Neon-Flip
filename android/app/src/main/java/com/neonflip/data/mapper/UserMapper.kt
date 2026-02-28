package com.neonflip.data.mapper

import com.neonflip.data.remote.dto.UserDto
import com.neonflip.domain.model.User

fun UserDto.toDomain(): User {
    return User(
        id = id,
        username = username,
        email = email,
        createdAt = createdAt
    )
}
