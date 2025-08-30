package com.example.umape.data.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.security.MessageDigest

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val passwordHash: String, // Almacenamos hash de la contraseña, no la contraseña directa
    val avatar: String = "default_avatar",
    val trainerLevel: Int = 1,
    val language: String = "es",
    val totalCoins: Int = 1000, // Empezamos con 1000 monedas
    val totalFans: Int = 0,
    val gamesPlayed: Int = 0,
    val racesWon: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val lastPlayed: Long = System.currentTimeMillis()
) {
    companion object {
        /**
         * Crea un hash seguro de la contraseña
         */
        fun hashPassword(password: String): String {
            val bytes = password.toByteArray()
            val md = MessageDigest.getInstance("SHA-256")
            val digest = md.digest(bytes)
            return digest.fold("", { str, it -> str + "%02x".format(it) })
        }

        /**
         * Verifica si una contraseña coincide con el hash
         */
        fun verifyPassword(password: String, hash: String): Boolean {
            return hashPassword(password) == hash
        }
    }
}