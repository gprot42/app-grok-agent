package com.tinygrok.client.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Tokyo Night inspired colors
private val TokyoNightDarkColorScheme = darkColorScheme(
    primary = Color(0xFFBB9AF7),
    onPrimary = Color(0xFF1A1B26),
    secondary = Color(0xFF7AA2F7),
    onSecondary = Color(0xFF1A1B26),
    background = Color(0xFF1A1B26),
    onBackground = Color(0xFFC0CAF5),
    surface = Color(0xFF16161E),
    onSurface = Color(0xFFC0CAF5),
    error = Color(0xFFF7768E),
    onError = Color(0xFF1A1B26)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6750A4),
    onPrimary = Color.White,
    secondary = Color(0xFF625B71),
    onSecondary = Color.White,
    background = Color(0xFFFFFBFE),
    onBackground = Color(0xFF1C1B1F),
    surface = Color(0xFFFFFBFE),
    onSurface = Color(0xFF1C1B1F)
)

@Composable
fun TinyGrokTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) TokyoNightDarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        content = content
    )
}
