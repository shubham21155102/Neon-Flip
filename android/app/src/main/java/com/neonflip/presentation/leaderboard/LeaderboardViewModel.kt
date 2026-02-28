package com.neonflip.presentation.leaderboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neonflip.domain.model.Result
import com.neonflip.domain.model.Score
import com.neonflip.domain.usecase.GetLeaderboardUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LeaderboardUiState(
    val scores: List<Score> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    private val getLeaderboardUseCase: GetLeaderboardUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LeaderboardUiState())
    val uiState: StateFlow<LeaderboardUiState> = _uiState.asStateFlow()

    init {
        loadLeaderboard()
    }

    fun loadLeaderboard() {
        viewModelScope.launch {
            _uiState.value = LeaderboardUiState(isLoading = true)

            when (val result = getLeaderboardUseCase()) {
                is Result.Success -> {
                    _uiState.value = LeaderboardUiState(
                        scores = result.data,
                        isLoading = false
                    )
                }
                is Result.Error -> {
                    _uiState.value = LeaderboardUiState(
                        isLoading = false,
                        errorMessage = result.error.message
                    )
                }
                is Result.Loading -> {
                    _uiState.value = LeaderboardUiState(isLoading = true)
                }
            }
        }
    }

    fun refresh() {
        loadLeaderboard()
    }
}
