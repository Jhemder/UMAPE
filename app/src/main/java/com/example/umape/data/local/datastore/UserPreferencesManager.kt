package com.example.umape.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferencesManager(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("user_prefs")
        private val USER_NAME = stringPreferencesKey("user_name")
        private val USER_AVATAR = stringPreferencesKey("user_avatar")
        private val TRAINER_LEVEL = intPreferencesKey("trainer_level")
        private val LANGUAGE = stringPreferencesKey("language")
        private val IS_FIRST_TIME = booleanPreferencesKey("is_first_time")
    }

    val isFirstTime: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[IS_FIRST_TIME] ?: true
    }

    val userName: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[USER_NAME] ?: ""
    }

    suspend fun saveUserData(
        name: String,
        avatar: String = "",
        language: String = "es"
    ) {
        context.dataStore.edit { preferences ->
            preferences[USER_NAME] = name
            preferences[USER_AVATAR] = avatar
            preferences[LANGUAGE] = language
            preferences[TRAINER_LEVEL] = 1
            preferences[IS_FIRST_TIME] = false
        }
    }
}