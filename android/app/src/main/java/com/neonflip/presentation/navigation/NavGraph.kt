package com.neonflip.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.neonflip.presentation.auth.LoginScreen
import com.neonflip.presentation.auth.LoginViewModel
import com.neonflip.presentation.auth.RegisterScreen
import com.neonflip.presentation.auth.RegisterViewModel
import com.neonflip.presentation.game.GameScreen
import com.neonflip.presentation.game.GameViewModel
import com.neonflip.presentation.leaderboard.LeaderboardScreen
import com.neonflip.presentation.leaderboard.LeaderboardViewModel

@Composable
fun NeonFlipNavGraph(
    navController: NavHostController = androidx.navigation.compose.rememberNavController()
) {
    val authViewModel: AuthViewModel = hiltViewModel()
    val authState by authViewModel.authState.collectAsState()

    LaunchedEffect(authState.isAuthenticated) {
        if (authState.isAuthenticated && authState.currentUser != null) {
            // User is authenticated, navigate to game
            if (navController.currentDestination?.route != Screen.Game.route) {
                navController.navigate(Screen.Game.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }
            }
        } else if (!authState.isAuthenticated &&
                   navController.currentDestination?.route == null) {
            // User is not authenticated, navigate to login
            navController.navigate(Screen.Login.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    authViewModel.checkAuthStatus()
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    authViewModel.checkAuthStatus()
                    navController.popBackStack()
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Game.route) {
            GameScreen(
                onNavigateToLeaderboard = {
                    navController.navigate(Screen.Leaderboard.route)
                },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Leaderboard.route) {
            LeaderboardScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
