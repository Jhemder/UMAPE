package com.example.umape.ui.navigation

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.umape.data.local.database.UmapeDatabase
import com.example.umape.data.repository.UserRepository
import com.example.umape.ui.screens.login.LoginScreen
import com.example.umape.ui.screens.menu.MenuScreen
import com.example.umape.ui.viewmodel.UserViewModel
import com.example.umape.ui.viewmodel.UserViewModelFactory

@Composable
fun UmapeNavigation(
    navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current
    val database = UmapeDatabase.getDatabase(context)
    val repository = UserRepository(database.userDao())

    val viewModel: UserViewModel = viewModel(
        factory = UserViewModelFactory(repository)
    )

    val currentUser by viewModel.currentUser.collectAsState()
    val isFirstTimeUser by viewModel.isFirstTimeUser.collectAsState()

    val startDestination = if (isFirstTimeUser) "login" else "menu"

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("login") {
            LoginScreen { name, language ->
                viewModel.createUser(name, language) {
                    navController.navigate("menu") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            }
        }

        composable("menu") {
            currentUser?.let { user ->
                MenuScreen(
                    userName = user.name,
                    trainerLevel = user.trainerLevel,
                    totalCoins = user.totalCoins,
                    onNavigateToGame = { /* TODO */ },
                    onNavigateToCollection = { /* TODO */ },
                    onNavigateToShop = { /* TODO */ },
                    onNavigateToMultiplayer = { /* TODO */ },
                    onNavigateToSettings = { /* TODO */ },
                    onNavigateToProfile = { /* TODO */ }
                )
            }
        }
    }
}