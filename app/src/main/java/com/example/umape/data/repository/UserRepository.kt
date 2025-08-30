package com.example.umape.data.repository

import com.example.umape.data.local.database.UserDao
import com.example.umape.data.local.database.UserEntity
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {

    fun getCurrentUser(): Flow<UserEntity?> = userDao.getCurrentUser()

    suspend fun registerUser(name: String, password: String, language: String = "es"): Result<Long> {
        return try {
            // Verificar si el usuario ya existe
            if (userDao.userExists(name)) {
                Result.failure(Exception("El nombre de usuario ya existe"))
            } else {
                val user = UserEntity(
                    name = name,
                    passwordHash = UserEntity.hashPassword(password),
                    language = language,
                    avatar = "default_avatar",
                    trainerLevel = 1,
                    totalCoins = 1000 // Bonus inicial
                )
                val userId = userDao.insertUser(user)
                Result.success(userId)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun loginUser(name: String, password: String): Result<UserEntity> {
        return try {
            val user = userDao.getUserByName(name)
            if (user != null && UserEntity.verifyPassword(password, user.passwordHash)) {
                // Actualizar último acceso
                userDao.updateLastPlayed(user.id, System.currentTimeMillis())
                Result.success(user)
            } else {
                Result.failure(Exception("Nombre de usuario o contraseña incorrectos"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateLastPlayed(userId: Long) {
        userDao.updateLastPlayed(userId, System.currentTimeMillis())
    }

    suspend fun addCoins(userId: Long, coins: Int) {
        userDao.addCoins(userId, coins)
    }

    suspend fun incrementGamesPlayed(userId: Long) {
        userDao.incrementGamesPlayed(userId)
    }

    suspend fun incrementRacesWon(userId: Long) {
        userDao.incrementRacesWon(userId)
    }

    suspend fun isFirstTimeUser(): Boolean {
        return userDao.getUserCount() == 0
    }

    suspend fun changePassword(userId: Long, oldPassword: String, newPassword: String): Result<Boolean> {
        return try {
            val user = userDao.getUserById(userId)
            if (user != null && UserEntity.verifyPassword(oldPassword, user.passwordHash)) {
                val updatedUser = user.copy(
                    passwordHash = UserEntity.hashPassword(newPassword)
                )
                userDao.updateUser(updatedUser)
                Result.success(true)
            } else {
                Result.failure(Exception("Contraseña actual incorrecta"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout() {
        // Aquí podrías limpiar datos específicos de sesión si los hay
        // Por ahora solo necesitamos que el Flow se actualice, lo cual sucede automáticamente
        // cuando el usuario actual ya no está marcado como "actual"
    }
}