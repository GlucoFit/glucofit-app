package com.fitcoders.glucofitapp.data

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
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
        private val ASSESSMENT_KEY = booleanPreferencesKey("assessment_complete")
        private val IN_ASSESSMENT_KEY = booleanPreferencesKey("in_assessment")


        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
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

    suspend fun getToken(): String {
        return dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            preferences[TOKEN_KEY] ?: ""
        }.first()
    }

    suspend fun login() {
        dataStore.edit { preferences ->
            preferences[STATE_KEY] = true
        }
    }

   /* suspend fun logout() {

        dataStore.edit { preferences ->
            preferences[STATE_KEY] = false
            preferences[USERNAME_KEY] = ""
            preferences[EMAIL_KEY] = ""
            preferences[TOKEN_KEY] = ""
        }
        Log.d("UserPreference", "Logout process completed. DataStore session cleared.")
    }*/

    // Method to logout and clear token
    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences[STATE_KEY] = false
            preferences[USERNAME_KEY] = ""
            preferences[EMAIL_KEY] = ""
            preferences[TOKEN_KEY] = "" // Clear token from DataStore
            preferences[IN_ASSESSMENT_KEY] = false
        }

        Log.d("UserPreference", "Logout process completed. DataStore session cleared.")

        // Verify the token is cleared
        val token = runBlocking {
            dataStore.data.map { preferences ->
                preferences[TOKEN_KEY] ?: ""
            }.first()
        }
        Log.d("UserPreference", "Token after logout: '$token'")
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

    suspend fun setAssessmentComplete() {
        dataStore.edit { preferences ->
            preferences[ASSESSMENT_KEY] = true
            preferences[IN_ASSESSMENT_KEY] = false
        }
    }

    fun isAssessmentComplete(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[ASSESSMENT_KEY] ?: false
        }
    }

    suspend fun setInAssessment(inAssessment: Boolean) {
        dataStore.edit { preferences ->
            preferences[IN_ASSESSMENT_KEY] = inAssessment
        }
    }

    fun isInAssessment(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[IN_ASSESSMENT_KEY] ?: false
        }
    }
}
