package com.neonflip.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neonflip.domain.model.Result
import com.neonflip.domain.usecase.SubmitScoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val submitScoreUseCase: SubmitScoreUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(GameState())
    val uiState: StateFlow<GameState> = _uiState.asStateFlow()

    private var gameEngine: GameEngine? = null
    private var isRunning = false

    fun initGame(screenWidth: Float, screenHeight: Float) {
        gameEngine = GameEngine(screenWidth, screenHeight)
        _uiState.value = GameState(
            highScore = _uiState.value.highScore
        )
    }

    fun startGame() {
        gameEngine?.startGame()
        _uiState.value = _uiState.value.copy(
            score = 0,
            isGameOver = false,
            isPlaying = true,
            isPaused = false
        )
        startGameLoop()
    }

    private fun startGameLoop() {
        if (isRunning) return
        isRunning = true

        viewModelScope.launch {
            while (isRunning && gameEngine != null) {
                val update = gameEngine!!.update()

                _uiState.value = _uiState.value.copy(
                    score = update.score,
                    isGameOver = update.isGameOver,
                    isPlaying = !update.isGameOver
                )

                if (update.isGameOver) {
                    isRunning = false
                    // Auto-submit score
                    submitScore(update.score)
                    break
                }

                // ~60 FPS
                delay(16)
            }
        }
    }

    fun flipGravity() {
        gameEngine?.flipGravity()
    }

    fun pauseGame() {
        isRunning = false
        gameEngine?.pauseGame()
        _uiState.value = _uiState.value.copy(isPaused = true)
    }

    fun resumeGame() {
        if (_uiState.value.isGameOver) return
        gameEngine?.resumeGame()
        _uiState.value = _uiState.value.copy(isPaused = false)
        startGameLoop()
    }

    private fun submitScore(score: Int) {
        viewModelScope.launch {
            when (submitScoreUseCase(score)) {
                is Result.Success -> {
                    // Score submitted successfully
                }
                is Result.Error -> {
                    // Score submission failed, but don't show error to user
                }
                is Result.Loading -> {
                    // Loading
                }
            }
        }
    }

    fun getPlayer(): Player? = gameEngine?.let {
        // Get current player state from last update
        Player(
            x = it.let { engine ->
                // We'll track this through the game loop
                100f
            },
            y = _uiState.value.let { state ->
                // This will be updated by the canvas
                500f
            }
        )
    }

    override fun onCleared() {
        super.onCleared()
        isRunning = false
    }
}
