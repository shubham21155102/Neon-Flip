package com.neonflip.presentation.game

import android.content.Context
import android.os.Build
import android.os.Vibrator
import android.os.VibrationEffect
import android.os.VibratorManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.neonflip.presentation.theme.NeonCyan
import com.neonflip.presentation.theme.NeonGreen
import com.neonflip.presentation.theme.NeonPink
import kotlinx.coroutines.delay
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalDensity
import kotlin.math.max
import kotlin.math.min

@Composable
fun GameScreen(
    onNavigateToLeaderboard: () -> Unit,
    onLogout: () -> Unit,
    viewModel: GameViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Initialize game with screen dimensions
    val density = LocalDensity.current
    var screenWidth by remember { mutableStateOf(400f) }
    var screenHeight by remember { mutableStateOf(800f) }

    LaunchedEffect(Unit) {
        with(density) {
            // Get screen size
            screenWidth = 400f
            screenHeight = 800f
            viewModel.initGame(screenWidth, screenHeight)
        }
    }

    // Haptic feedback on gravity flip
    fun triggerHapticFeedback() {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vm = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vm.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    50L,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(50L)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background
        androidx.compose.foundation.background(
            color = Color.Black,
            modifier = Modifier.fillMaxSize()
        )

        when {
            !uiState.isPlaying && !uiState.isGameOver -> {
                // Start screen
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "NEON FLIP",
                        style = MaterialTheme.typography.displayLarge,
                        color = NeonCyan
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Tap to flip gravity and avoid obstacles!",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(48.dp))

                    Button(
                        onClick = {
                            viewModel.startGame()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NeonGreen,
                            contentColor = Color.Black
                        )
                    ) {
                        Text(
                            "START GAME",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Button(
                            onClick = onNavigateToLeaderboard,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = NeonPink,
                                contentColor = Color.Black
                            )
                        ) {
                            Text("LEADERBOARD")
                        }

                        Button(
                            onClick = onLogout,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Gray,
                                contentColor = Color.White
                            )
                        ) {
                            Text("LOGOUT")
                        }
                    }
                }
            }

            uiState.isGameOver -> {
                // Game over screen
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "GAME OVER",
                        style = MaterialTheme.typography.displayLarge,
                        color = NeonPink
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Score: ${uiState.score}",
                        style = MaterialTheme.typography.displayMedium,
                        color = NeonCyan
                    )

                    if (uiState.score > uiState.highScore && uiState.highScore > 0) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "New High Score!",
                            style = MaterialTheme.typography.titleMedium,
                            color = NeonGreen
                        )
                    }

                    Spacer(modifier = Modifier.height(48.dp))

                    Button(
                        onClick = {
                            viewModel.startGame()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NeonGreen,
                            contentColor = Color.Black
                        )
                    ) {
                        Text(
                            "PLAY AGAIN",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = onNavigateToLeaderboard,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NeonPink,
                            contentColor = Color.Black
                        )
                    ) {
                        Text("LEADERBOARD")
                    }
                }
            }

            else -> {
                // Game is playing
                GameCanvas(
                    gameState = uiState,
                    onGravityFlip = {
                        viewModel.flipGravity()
                        triggerHapticFeedback()
                    },
                    modifier = Modifier.fillMaxSize()
                )

                // Top bar with score and menu
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .align(Alignment.TopCenter),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Score: ${uiState.score}",
                        style = MaterialTheme.typography.titleLarge,
                        color = NeonCyan
                    )

                    Row {
                        IconButton(
                            onClick = {
                                if (uiState.isPaused) {
                                    viewModel.resumeGame()
                                } else {
                                    viewModel.pauseGame()
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (uiState.isPaused) {
                                    androidx.compose.material.icons.Icons.Filled.PlayArrow
                                } else {
                                    androidx.compose.material.icons.Icons.Filled.Pause
                                },
                                contentDescription = if (uiState.isPaused) "Resume" else "Pause",
                                tint = NeonCyan,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        IconButton(onClick = onLogout) {
                            Icon(
                                imageVector = androidx.compose.material.icons.Icons.Filled.ExitToApp,
                                contentDescription = "Logout",
                                tint = NeonPink,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }

                // Pause overlay
                if (uiState.isPaused) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.7f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "PAUSED",
                                style = MaterialTheme.typography.displayLarge,
                                color = NeonCyan
                            )

                            Spacer(modifier = Modifier.height(32.dp))

                            Button(
                                onClick = { viewModel.resumeGame() },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = NeonGreen,
                                    contentColor = Color.Black
                                )
                            ) {
                                Text("RESUME")
                            }
                        }
                    }
                }
            }
        }
    }
}
