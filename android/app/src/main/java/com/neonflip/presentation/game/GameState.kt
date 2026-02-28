package com.neonflip.presentation.game

data class GameState(
    val score: Int = 0,
    val isGameOver: Boolean = false,
    val isPlaying: Boolean = false,
    val isPaused: Boolean = false,
    val highScore: Int = 0
)

enum class Gravity {
    UP, DOWN
}

data class Player(
    val x: Float,
    val y: Float,
    val size: Float = 50f,
    val velocityY: Float = 0f
)

data class Obstacle(
    val x: Float,
    val y: Float,
    val width: Float,
    val height: Float,
    val passed: Boolean = false
)
