package com.example.umape.data.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val avatar: String = "",
    val trainerLevel: Int = 1,
    val language: String = "es",
    val totalCoins: Int = 0,
    val totalFans: Int = 0,
    val gamesPlayed: Int = 0,
    val racesWon: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val lastPlayed: Long = System.currentTimeMillis()
)