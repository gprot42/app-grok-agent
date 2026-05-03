package com.tinygrok.client.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tinygrok.client.data.SettingsRepository
import com.tinygrok.client.ui.theme.ThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _apiKey = MutableStateFlow("")
    val apiKey: StateFlow<String> = _apiKey.asStateFlow()

    private val _themeMode = MutableStateFlow(ThemeMode.SYSTEM)
    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    init {
        viewModelScope.launch {
            settingsRepository.apiKey.collect { key ->
                _apiKey.value = key
            }
            settingsRepository.themeMode.collect { mode ->
                _themeMode.value = mode
            }
        }
    }

    fun saveApiKey(key: String) {
        viewModelScope.launch {
            settingsRepository.saveApiKey(key)
            _apiKey.value = key
        }
    }

    fun saveThemeMode(mode: ThemeMode) {
        viewModelScope.launch {
            settingsRepository.saveThemeMode(mode)
            _themeMode.value = mode
        }
    }
}
