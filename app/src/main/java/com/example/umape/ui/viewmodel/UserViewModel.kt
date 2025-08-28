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

    init {
        checkIfFirstTimeUser()
    }

    private fun checkIfFirstTimeUser() {
        viewModelScope.launch {
            _isFirstTimeUser.value = repository.isFirstTimeUser()
        }
    }

    fun createUser(name: String, language: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.createUser(name, language)
            _isFirstTimeUser.value = false
            onSuccess()
        }
    }

    fun updateLastPlayed(userId: Long) {
        viewModelScope.launch {
            repository.updateLastPlayed(userId)
        }
    }
}