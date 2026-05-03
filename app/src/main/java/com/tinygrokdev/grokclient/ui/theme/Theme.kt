package com.aicoder.grokclient.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import com.aicoder.grokclient.data.local.SettingsRepository
import kotlinx.coroutines.flow.first

enum class AppTheme {
    LIGHT, DARK, TOKYO_NIGHT
}

@Composable
fun TinyGrokTheme(
    appTheme: AppTheme = AppTheme.DARK,
    content: @Composable () -> Unit
) {
    val colorScheme = when (appTheme) {
        AppTheme.LIGHT -> lightColorScheme(
            primary = LightPrimary,
            onPrimary = LightOnPrimary,
            background = LightBackground,
            surface = LightSurface,
            onSurface = LightOnSurface
        )
        AppTheme.DARK -> darkColorScheme(
            primary = DarkPrimary,
            onPrimary = DarkOnPrimary,
            background = DarkBackground,
            surface = DarkSurface,
            onSurface = DarkOnSurface
        )
        AppTheme.TOKYO_NIGHT -> darkColorScheme(
            primary = TokyoNightPrimary,
            onPrimary = TokyoNightOnPrimary,
            background = TokyoNightBackground,
            surface = TokyoNightSurface,
            onSurface = TokyoNightOnSurface,
            secondary = TokyoNightSecondary,
            onSecondary = TokyoNightOnSecondary,
            tertiary = TokyoNightAccent,
            onTertiary = TokyoNightText
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        content = content
    )
}
