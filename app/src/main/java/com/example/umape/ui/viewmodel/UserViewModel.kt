package com.example.umape.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.umape.data.local.database.UserEntity
import com.example.umape.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UserViewModel(private val repository: UserRepository) : ViewModel() {

    val currentUser: StateFlow<UserEntity?> = repository.getCurrentUser()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null
        )

    private val _isFirstTimeUser = MutableStateFlow(true)
    val isFirstTimeUser: StateFlow<Boolean> = _isFirstTimeUser.asStateFlow()

    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> = _loginError.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _availableUsers = MutableStateFlow<List<UserEntity>>(emptyList())
    val availableUsers: StateFlow<List<UserEntity>> = _availableUsers.asStateFlow()

    init {
        checkIfFirstTimeUser()
        loadAvailableUsers()
    }

    private fun checkIfFirstTimeUser() {
        viewModelScope.launch {
            _isFirstTimeUser.value = repository.isFirstTimeUser()
        }
    }

    private fun loadAvailableUsers() {
        viewModelScope.launch {
            try {
                _availableUsers.value = repository.getAllUsers()
            } catch (e: Exception) {
                // Manejar error silenciosamente
                _availableUsers.value = emptyList()
            }
        }
    }

    fun registerUser(name: String, password: String, language: String = "es", onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _loginError.value = null

            repository.registerUser(name, password, language)
                .onSuccess {
                    _isFirstTimeUser.value = false
                    loadAvailableUsers()
                    onResult(true, null)
                }
                .onFailure { error ->
                    val errorMessage = error.message ?: "Error desconocido"
                    _loginError.value = errorMessage
                    onResult(false, errorMessage)
                }

            _isLoading.value = false
        }
    }

    fun loginUser(name: String, password: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _loginError.value = null

            repository.loginUser(name, password)
                .onSuccess {
                    loadAvailableUsers()
                    onResult(true, null)
                }
                .onFailure { error ->
                    val errorMessage = error.message ?: "Error de login"
                    _loginError.value = errorMessage
                    onResult(false, errorMessage)
                }

            _isLoading.value = false
        }
    }

    fun switchUser(user: UserEntity, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                // Actualizar lastPlayed del usuario seleccionado
                repository.updateLastPlayed(user.id)
                onResult(true)
            } catch (e: Exception) {
                onResult(false)
            }
        }
    }

    fun logout(onResult: (Boolean) -> Unit = {}) {
        viewModelScope.launch {
            try {
                // Llamar al método logout del repositorio
                repository.logout()
                    .onSuccess {
                        // Limpiar estados locales
                        _loginError.value = null
                        _isLoading.value = false
                        onResult(true)
                    }
                    .onFailure {
                        onResult(false)
                    }
            } catch (e: Exception) {
                onResult(false)
            }
        }
    }

    fun updateLastPlayed(userId: Long) {
        viewModelScope.launch {
            repository.updateLastPlayed(userId)
        }
    }

    fun addCoins(userId: Long, coins: Int) {
        viewModelScope.launch {
            repository.addCoins(userId, coins)
        }
    }

    fun incrementGamesPlayed(userId: Long) {
        viewModelScope.launch {
            repository.incrementGamesPlayed(userId)
        }
    }

    fun incrementRacesWon(userId: Long) {
        viewModelScope.launch {
            repository.incrementRacesWon(userId)
        }
    }

    fun clearLoginError() {
        _loginError.value = null
    }

    fun changePassword(userId: Long, oldPassword: String, newPassword: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            repository.changePassword(userId, oldPassword, newPassword)
                .onSuccess {
                    onResult(true, null)
                }
                .onFailure { error ->
                    onResult(false, error.message)
                }
        }
    }
}