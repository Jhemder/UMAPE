package com.example.umape.data.repository

import com.example.umape.data.local.database.UserDao
import com.example.umape.data.local.database.UserEntity
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {

    fun getCurrentUser(): Flow<UserEntity?> = userDao.getCurrentUser()

    suspend fun createUser(name: String, language: String): Long {
        val user = UserEntity(
            name = name,
            language = language,
            avatar = "default_avatar",
            trainerLevel = 1
        )
        return userDao.insertUser(user)
    }

    suspend fun updateLastPlayed(userId: Long) {
        userDao.updateLastPlayed(userId, System.currentTimeMillis())
    }

    suspend fun addCoins(userId: Long, coins: Int) {
        userDao.addCoins(userId, coins)
    }

    suspend fun isFirstTimeUser(): Boolean {
        return userDao.getUserCount() == 0
    }
}