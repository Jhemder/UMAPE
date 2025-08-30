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
import com.example.umape.ui.screens.userselection.UserSelectionScreen
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
    val availableUsers by viewModel.availableUsers.collectAsState()
    val isFirstTimeUser by viewModel.isFirstTimeUser.collectAsState()
    var showSplash by remember { mutableStateOf(true) }

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen {
                showSplash = false
                val nextDestination = if (currentUser == null) "login" else "menu"
                navController.navigate(nextDestination) {
                    popUpTo("splash") { inclusive = true }
                }
            }
        }

        composable("login") {
            LoginScreen { name, password, isRegister ->
                if (isRegister) {
                    viewModel.registerUser(name, password) { success, error ->
                        if (success) {
                            navController.navigate("menu") {
                                popUpTo("login") { inclusive = true }
                            }
                        } else {
                            // TODO: Mostrar error (ya está en el ViewModel)
                        }
                    }
                } else {
                    viewModel.loginUser(name, password) { success, error ->
                        if (success) {
                            navController.navigate("menu") {
                                popUpTo("login") { inclusive = true }
                            }
                        } else {
                            // TODO: Mostrar error (ya está en el ViewModel)
                        }
                    }
                }
            }
        }

        composable("user_selection") {
            UserSelectionScreen(
                users = availableUsers,
                currentUser = currentUser,
                onUserSelected = { user ->
                    viewModel.switchUser(user) { success ->
                        if (success) {
                            navController.navigate("menu") {
                                popUpTo("user_selection") { inclusive = true }
                            }
                        }
                    }
                },
                onCreateNewUser = {
                    navController.navigate("login")
                },
                onBackToMenu = {
                    navController.navigate("menu") {
                        popUpTo("user_selection") { inclusive = true }
                    }
                }
            )
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
                    onSwitchUser = {
                        // NUEVA FUNCIONALIDAD: Cambiar de usuario
                        navController.navigate("user_selection")
                    },
                    onLogout = {
                        // Logout completo: volver al login
                        viewModel.logout { success ->
                            navController.navigate("login") {
                                popUpTo("menu") { inclusive = true }
                            }
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