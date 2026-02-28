package com.neonflip.presentation.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neonflip.domain.model.Result
import com.neonflip.domain.model.User
import com.neonflip.domain.usecase.GetCurrentUserUseCase
import com.neonflip.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthState(
    val isAuthenticated: Boolean = false,
    val currentUser: User? = null,
    val isLoading: Boolean = true
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    init {
        checkAuthStatus()
    }

    fun checkAuthStatus() {
        viewModelScope.launch {
            _authState.value = AuthState(isLoading = true)
            val isLoggedIn = authRepository.isLoggedIn()

            if (isLoggedIn) {
                when (val result = getCurrentUserUseCase()) {
                    is Result.Success -> {
                        _authState.value = AuthState(
                            isAuthenticated = true,
                            currentUser = result.data,
                            isLoading = false
                        )
                    }
                    is Result.Error -> {
                        _authState.value = AuthState(
                            isAuthenticated = false,
                            isLoading = false
                        )
                    }
                    is Result.Loading -> {
                        _authState.value = AuthState(isLoading = true)
                    }
                }
            } else {
                _authState.value = AuthState(
                    isAuthenticated = false,
                    isLoading = false
                )
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _authState.value = AuthState(
                isAuthenticated = false,
                currentUser = null,
                isLoading = false
            )
        }
    }
}
