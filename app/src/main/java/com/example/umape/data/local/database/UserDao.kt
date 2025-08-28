package com.example.umape.data.local.database

import androidx.room.*
import androidx.room.OnConflictStrategy
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM users ORDER BY lastPlayed DESC LIMIT 1")
    fun getCurrentUser(): Flow<UserEntity?>

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: Long): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity): Long

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("UPDATE users SET lastPlayed = :timestamp WHERE id = :userId")
    suspend fun updateLastPlayed(userId: Long, timestamp: Long)

    @Query("UPDATE users SET totalCoins = totalCoins + :coins WHERE id = :userId")
    suspend fun addCoins(userId: Long, coins: Int)

    @Query("SELECT COUNT(*) FROM users")
    suspend fun getUserCount(): Int
}