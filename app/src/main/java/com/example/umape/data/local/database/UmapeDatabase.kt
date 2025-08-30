package com.example.umape.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import android.content.Context

@Database(
    entities = [UserEntity::class],
    version = 2, // Incrementamos la versión por el cambio de esquema
    exportSchema = false
)
abstract class UmapeDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: UmapeDatabase? = null

        // Migración de versión 1 a 2 (agregar campo passwordHash)
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Agregar el campo passwordHash con un valor por defecto
                database.execSQL("ALTER TABLE users ADD COLUMN passwordHash TEXT NOT NULL DEFAULT ''")

                // Actualizar totalCoins por defecto a 1000 para usuarios existentes
                database.execSQL("UPDATE users SET totalCoins = 1000 WHERE totalCoins = 0")
            }
        }

        fun getDatabase(context: Context): UmapeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UmapeDatabase::class.java,
                    "umape_database"
                )
                    .addMigrations(MIGRATION_1_2) // Agregar la migración
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}