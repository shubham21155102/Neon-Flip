package com.neonflip.presentation.game

import kotlinx.coroutines.delay
import kotlin.math.abs
import kotlin.random.Random

class GameEngine(
    private val screenWidth: Float,
    private val screenHeight: Float
) {
    private var player = Player(
        x = screenWidth * 0.2f,
        y = screenHeight / 2
    )
    private var obstacles = mutableListOf<Obstacle>()
    private var score = 0
    private var gravity = Gravity.DOWN
    private var isGameOver = false
    private var isPlaying = false

    companion object {
        private const val GRAVITY_FORCE = 0.5f
        private const val JUMP_FORCE = -8f
        private const val MAX_FALL_SPEED = 15f
        private const val OBSTACLE_SPEED = 5f
        private const val OBSTACLE_SPAWN_INTERVAL = 1500L
        private const val OBSTACLE_GAP = 200f
        private const val OBSTACLE_MIN_HEIGHT = 100f
    }

    fun startGame() {
        player = Player(
            x = screenWidth * 0.2f,
            y = screenHeight / 2
        )
        obstacles.clear()
        score = 0
        gravity = Gravity.DOWN
        isGameOver = false
        isPlaying = true
    }

    fun flipGravity() {
        if (!isPlaying || isGameOver) return

        gravity = if (gravity == Gravity.DOWN) Gravity.UP else Gravity.DOWN

        // Apply immediate velocity boost on flip
        player = player.copy(
            velocityY = if (gravity == Gravity.DOWN) JUMP_FORCE else -JUMP_FORCE
        )
    }

    fun update(): GameUpdate {
        if (!isPlaying || isGameOver) {
            return GameUpdate(
                player = player,
                obstacles = obstacles.toList(),
                score = score,
                isGameOver = isGameOver
            )
        }

        // Update player position based on gravity
        val newVelocityY = when (gravity) {
            Gravity.DOWN -> minOf(player.velocityY + GRAVITY_FORCE, MAX_FALL_SPEED)
            Gravity.UP -> maxOf(player.velocityY - GRAVITY_FORCE, -MAX_FALL_SPEED)
        }

        var newY = player.y + newVelocityY

        // Boundary checks - player can't go off screen vertically
        newY = newY.coerceIn(0f, screenHeight - player.size)

        player = player.copy(
            y = newY,
            velocityY = newVelocityY
        )

        // Spawn obstacles
        if (obstacles.isEmpty() || obstacles.last().x < screenWidth - OBSTACLE_SPAWN_INTERVAL * OBSTACLE_SPEED / 16f) {
            spawnObstacle()
        }

        // Update obstacles
        val iterator = obstacles.iterator()
        while (iterator.hasNext()) {
            val obstacle = iterator.next()
            val newX = obstacle.x - OBSTACLE_SPEED

            if (newX + obstacle.width < 0) {
                iterator.remove()
                continue
            }

            // Check if player passed obstacle
            if (!obstacle.passed && obstacle.x + obstacle.width < player.x) {
                score++
                iterator.remove()
                continue
            }

            // Collision detection
            if (checkCollision(player, obstacle)) {
                isGameOver = true
                isPlaying = false
            }

            obstacles[obstacles.indexOf(obstacle)] = obstacle.copy(x = newX)
        }

        return GameUpdate(
            player = player,
            obstacles = obstacles.toList(),
            score = score,
            isGameOver = isGameOver
        )
    }

    private fun spawnObstacle() {
        val gapY = Random.nextFloat() * (screenHeight - OBSTACLE_GAP - 2 * OBSTACLE_MIN_HEIGHT) + OBSTACLE_MIN_HEIGHT
        val obstacleWidth = 60f

        // Top obstacle
        val topObstacle = Obstacle(
            x = screenWidth,
            y = 0f,
            width = obstacleWidth,
            height = gapY
        )

        // Bottom obstacle
        val bottomObstacle = Obstacle(
            x = screenWidth,
            y = gapY + OBSTACLE_GAP,
            width = obstacleWidth,
            height = screenHeight - gapY - OBSTACLE_GAP
        )

        obstacles.add(topObstacle)
        obstacles.add(bottomObstacle)
    }

    private fun checkCollision(player: Player, obstacle: Obstacle): Boolean {
        return player.x < obstacle.x + obstacle.width &&
                player.x + player.size > obstacle.x &&
                player.y < obstacle.y + obstacle.height &&
                player.y + player.size > obstacle.y
    }

    fun pauseGame() {
        isPlaying = false
    }

    fun resumeGame() {
        if (!isGameOver) {
            isPlaying = true
        }
    }

    fun getScore(): Int = score

    fun isGameOver(): Boolean = isGameOver

    fun isPlaying(): Boolean = isPlaying
}

data class GameUpdate(
    val player: Player,
    val obstacles: List<Obstacle>,
    val score: Int,
    val isGameOver: Boolean
)
