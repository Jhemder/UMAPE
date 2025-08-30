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

    @Query("SELECT * FROM users WHERE name = :name")
    suspend fun getUserByName(name: String): UserEntity?

    @Query("SELECT * FROM users ORDER BY lastPlayed DESC")
    suspend fun getAllUsers(): List<UserEntity>

    @Insert(onConflict = OnConflictStrategy.ABORT) // No permitir nombres duplicados
    suspend fun insertUser(user: UserEntity): Long

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("UPDATE users SET lastPlayed = :timestamp WHERE id = :userId")
    suspend fun updateLastPlayed(userId: Long, timestamp: Long)

    @Query("UPDATE users SET totalCoins = totalCoins + :coins WHERE id = :userId")
    suspend fun addCoins(userId: Long, coins: Int)

    @Query("UPDATE users SET gamesPlayed = gamesPlayed + 1 WHERE id = :userId")
    suspend fun incrementGamesPlayed(userId: Long)

    @Query("UPDATE users SET racesWon = racesWon + 1 WHERE id = :userId")
    suspend fun incrementRacesWon(userId: Long)

    @Query("SELECT COUNT(*) FROM users")
    suspend fun getUserCount(): Int

    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE name = :name)")
    suspend fun userExists(name: String): Boolean

    @Query("DELETE FROM users WHERE name = :name")
    suspend fun deleteUserByName(name: String)

    @Query("UPDATE users SET lastPlayed = 0 WHERE id != :currentUserId")
    suspend fun logoutOtherUsers(currentUserId: Long)

    @Query("UPDATE users SET lastPlayed = 0")
    suspend fun logoutAllUsers()
}