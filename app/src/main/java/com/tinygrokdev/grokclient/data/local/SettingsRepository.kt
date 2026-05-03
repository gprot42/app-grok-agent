package com.aicoder.grokclient.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.aicoder.grokclient.ui.theme.AppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class SettingsRepository @Inject constructor(
    private val context: Context
) {
    private val API_KEY_KEY = stringPreferencesKey("api_key")
    private val THEME_KEY = stringPreferencesKey("theme")

    val apiKey: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[API_KEY_KEY] }

    val theme: Flow<AppTheme> = context.dataStore.data
        .map { preferences ->
            when (preferences[THEME_KEY]) {
                "LIGHT" -> AppTheme.LIGHT
                "TOKYO_NIGHT" -> AppTheme.TOKYO_NIGHT
                else -> AppTheme.DARK
            }
        }

    suspend fun saveApiKey(key: String) {
        context.dataStore.edit { preferences ->
            preferences[API_KEY_KEY] = key
        }
    }

    suspend fun clearApiKey() {
        context.dataStore.edit { preferences ->
            preferences.remove(API_KEY_KEY)
        }
    }

    suspend fun saveTheme(theme: AppTheme) {
        context.dataStore.edit { preferences ->
            preferences[THEME_KEY] = theme.name
        }
    }
}
