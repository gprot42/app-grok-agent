package com.tinygrok.client.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.tinygrok.client.ui.theme.ThemeMode
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "tiny_grok_settings")

@Singleton
class SettingsRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object PreferencesKeys {
        val API_KEY = stringPreferencesKey("api_key")
        val THEME_MODE = stringPreferencesKey("theme_mode")
    }

    val apiKey: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.API_KEY] ?: ""
        }

    val themeMode: Flow<ThemeMode> = context.dataStore.data
        .map { preferences ->
            val modeStr = preferences[PreferencesKeys.THEME_MODE] ?: ThemeMode.SYSTEM.name
            try {
                ThemeMode.valueOf(modeStr)
            } catch (e: IllegalArgumentException) {
                ThemeMode.SYSTEM
            }
        }

    suspend fun saveApiKey(key: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.API_KEY] = key
        }
    }

    suspend fun saveThemeMode(mode: ThemeMode) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.THEME_MODE] = mode.name
        }
    }
}
