package com.neonflip.presentation.game

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import com.neonflip.presentation.theme.NeonCyan
import com.neonflip.presentation.theme.NeonGreen
import com.neonflip.presentation.theme.NeonPink
import kotlinx.coroutines.delay
import kotlin.math.min

@Composable
fun GameCanvas(
    gameState: GameState,
    onGravityFlip: () -> Unit,
    modifier: Modifier = Modifier
) {
    var playerY by remember { mutableStateOf(500f) }
    var playerVelocityY by remember { mutableStateOf(0f) }
    var gravity by remember { mutableStateOf(Gravity.DOWN) }
    var obstacles by remember { mutableStateOf(listOf<Obstacle>()) }
    var currentScore by remember { mutableStateOf(0) }
    var screenHeightPx by remember { mutableStateOf(1000f) }
    var screenWidthPx by remember { mutableStateOf(400f) }

    val density = LocalDensity.current
    var screenCenterY by remember { mutableStateOf(500f) }

    // Screen size detection
    LaunchedEffect(Unit) {
        with(density) {
            // These will be set by the Box size
        }
    }

    // Game loop for rendering
    LaunchedEffect(gameState.isPlaying, gameState.isPaused, gameState.isGameOver) {
        if (gameState.isPlaying && !gameState.isPaused) {
            // Simulate game physics for rendering
            while (gameState.isPlaying && !gameState.isPaused && !gameState.isGameOver) {
                val gravityForce = 0.5f
                val jumpForce = -8f
                val maxFallSpeed = 15f

                // Apply gravity
                playerVelocityY = when (gravity) {
                    Gravity.DOWN -> minOf(playerVelocityY + gravityForce, maxFallSpeed)
                    Gravity.UP -> maxOf(playerVelocityY - gravityForce, -maxFallSpeed)
                }

                playerY += playerVelocityY

                // Boundary checks
                playerY = playerY.coerceIn(0f, screenHeightPx - 50f)

                // Move obstacles
                obstacles = obstacles.map { obstacle ->
                    obstacle.copy(x = obstacle.x - 5f)
                }.filter { it.x + it.width > 0 }

                // Spawn obstacles periodically
                if (obstacles.isEmpty() || obstacles.last().x < screenWidthPx - 400f) {
                    val gapY = (Math.random() * (screenHeightPx - 400f)).toFloat() + 100f
                    val obstacleWidth = 60f

                    val topObstacle = Obstacle(
                        x = screenWidthPx,
                        y = 0f,
                        width = obstacleWidth,
                        height = gapY
                    )

                    val bottomObstacle = Obstacle(
                        x = screenWidthPx,
                        y = gapY + 200f,
                        width = obstacleWidth,
                        height = screenHeightPx - gapY - 200f
                    )

                    obstacles = obstacles + listOf(topObstacle, bottomObstacle)
                }

                // Update score when passing obstacles
                obstacles.forEach { obstacle ->
                    if (!obstacle.passed && obstacle.x + obstacle.width < 100f) {
                        currentScore++
                        obstacles = obstacles.map {
                            if (it == obstacle) it.copy(passed = true)
                            else it
                        }
                    }
                }

                delay(16) // ~60 FPS
            }
        }
    }

    LaunchedEffect(gameState.score) {
        if (gameState.score != currentScore && currentScore < gameState.score) {
            currentScore = gameState.score
        }
    }

    LaunchedEffect(gameState.isGameOver) {
        if (gameState.isGameOver) {
            // Reset for new game
            playerY = screenCenterY
            playerVelocityY = 0f
            gravity = Gravity.DOWN
            obstacles = emptyList()
            currentScore = 0
        }
    }

    Box(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            onGravityFlip()
                            gravity = if (gravity == Gravity.DOWN) Gravity.UP else Gravity.DOWN
                            playerVelocityY = if (gravity == Gravity.DOWN) -8f else 8f
                        }
                    )
                }
        ) {
            screenWidthPx = size.width
            screenHeightPx = size.height
            screenCenterY = size.height / 2

            // Draw grid lines for neon effect
            val gridSize = 50f
            for (x in 0..size.width.toInt() step gridSize.toInt()) {
                drawLine(
                    color = Color.DarkGray.copy(alpha = 0.3f),
                    start = Offset(x.toFloat(), 0f),
                    end = Offset(x.toFloat(), size.height),
                    strokeWidth = 1f
                )
            }
            for (y in 0..size.height.toInt() step gridSize.toInt()) {
                drawLine(
                    color = Color.DarkGray.copy(alpha = 0.3f),
                    start = Offset(0f, y.toFloat()),
                    end = Offset(size.width, y.toFloat()),
                    strokeWidth = 1f
                )
            }

            // Draw obstacles
            obstacles.forEach { obstacle ->
                drawRoundRect(
                    color = NeonPink,
                    topLeft = Offset(obstacle.x, obstacle.y),
                    size = androidx.compose.ui.geometry.Size(obstacle.width, obstacle.height),
                    style = Stroke(width = 3f)
                )
                drawRoundRect(
                    color = NeonPink.copy(alpha = 0.3f),
                    topLeft = Offset(obstacle.x, obstacle.y),
                    size = androidx.compose.ui.geometry.Size(obstacle.width, obstacle.height)
                )
            }

            // Draw player (green square)
            drawRoundRect(
                color = NeonGreen,
                topLeft = Offset(100f, playerY),
                size = androidx.compose.ui.geometry.Size(50f, 50f),
                style = Stroke(width = 3f)
            )
            drawRoundRect(
                color = NeonGreen.copy(alpha = 0.7f),
                topLeft = Offset(100f, playerY),
                size = androidx.compose.ui.geometry.Size(50f, 50f)
            )

            // Draw glow effect around player
            drawCircle(
                color = NeonGreen.copy(alpha = 0.2f),
                radius = 40f,
                center = Offset(125f, playerY + 25f)
            )
        }

        // Score display
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp),
            contentAlignment = androidx.compose.ui.Alignment.TopCenter
        ) {
            androidx.compose.material3.Text(
                text = "Score: ${gameState.score}",
                style = androidx.compose.material3.MaterialTheme.typography.displayLarge,
                color = NeonCyan
            )
        }
    }
}
