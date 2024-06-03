package com.fitcoders.glucofitapp.data

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")
class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null
        private val USERNAME_KEY = stringPreferencesKey("username")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val STATE_KEY = booleanPreferencesKey("isLogin")
        private val ONBOARDING_KEY = booleanPreferencesKey("onboarding_complete")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

    fun getSession(): Flow<UserModel> {
        return dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            UserModel(
                username = preferences[USERNAME_KEY] ?: "",
                email = preferences[EMAIL_KEY] ?: "",
                token = preferences[TOKEN_KEY] ?: "",
                isLogin = preferences[STATE_KEY] ?: false
            )
        }
    }

    suspend fun saveSession(user: UserModel) {
        dataStore.edit { preferences ->
            preferences[USERNAME_KEY] = user.username
            preferences[EMAIL_KEY] = user.email
            preferences[TOKEN_KEY] = user.token
            preferences[STATE_KEY] = user.isLogin
        }
    }

    suspend fun login() {
        dataStore.edit { preferences ->
            preferences[STATE_KEY] = true
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences[STATE_KEY] = false
            preferences[USERNAME_KEY] = ""
            preferences[EMAIL_KEY] = ""
            preferences[TOKEN_KEY] = ""
        }
    }

    suspend fun setOnboardingComplete() {
        dataStore.edit { preferences ->
            preferences[ONBOARDING_KEY] = true
        }
    }

    fun isOnboardingComplete(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[ONBOARDING_KEY] ?: false
        }
    }
}
