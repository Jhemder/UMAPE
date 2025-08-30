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
import com.example.umape.ui.screens.splash.SplashScreen
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
    var showSplash by remember { mutableStateOf(true) }

    // Siempre empezar con splash
    val startDestination = "splash"

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("splash") {
            SplashScreen {
                showSplash = false
                // Determinar siguiente pantalla después del splash
                val nextDestination = if (currentUser == null) "login" else "menu"
                navController.navigate(nextDestination) {
                    popUpTo("splash") { inclusive = true }
                }
            }
        }

        composable("login") {
            LoginScreen { name, password, isRegister ->
                if (isRegister) {
                    // Modo registro - crear nuevo usuario
                    viewModel.registerUser(name, password) { success ->
                        if (success) {
                            navController.navigate("menu") {
                                popUpTo("login") { inclusive = true }
                            }
                        } else {
                            // Manejar error de registro
                            // TODO: Mostrar mensaje de error
                        }
                    }
                } else {
                    // Modo login - verificar usuario existente
                    viewModel.loginUser(name, password) { success ->
                        if (success) {
                            navController.navigate("menu") {
                                popUpTo("login") { inclusive = true }
                            }
                        } else {
                            // Manejar error de login
                            // TODO: Mostrar mensaje de error
                        }
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
                    onNavigateToGame = {
                        // TODO: Implementar navegación al juego
                        // navController.navigate("game")
                    },
                    onNavigateToCollection = {
                        // TODO: Implementar navegación a colección
                        // navController.navigate("collection")
                    },
                    onNavigateToShop = {
                        // TODO: Implementar navegación a tienda
                        // navController.navigate("shop")
                    },
                    onNavigateToMultiplayer = {
                        // TODO: Implementar navegación a multijugador
                        // navController.navigate("multiplayer")
                    },
                    onNavigateToSettings = {
                        // TODO: Implementar navegación a configuración
                        // navController.navigate("settings")
                    },
                    onNavigateToProfile = {
                        // TODO: Implementar navegación a perfil
                        // navController.navigate("profile")
                    },
                    onLogout = {
                        // Logout: volver al login
                        viewModel.logout()
                        navController.navigate("login") {
                            popUpTo("menu") { inclusive = true }
                        }
                    }
                )
            }
        }

        // TODO: Agregar más pantallas aquí
        // composable("game") { GameScreen() }
        // composable("collection") { CollectionScreen() }
        // composable("shop") { ShopScreen() }
        // etc.
    }
}