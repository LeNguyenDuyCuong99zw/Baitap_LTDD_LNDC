package com.example.pushnotifireuth.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    // UTH green (assumption: using #2E7D32 for "xanh lá UTH")
    primary = Color(0xFF2E7D32),
    secondary = Color(0xFF2196F3),
    tertiary = Color(0xFFFF9800),
    background = Color(0xFF2E7D32),
    surface = Color(0xFF121212),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
)

private val LightColorScheme = lightColorScheme(
    // UTH green used as primary and app background
    primary = Color(0xFF2E7D32),
    secondary = Color(0xFF2196F3),
    tertiary = Color(0xFFFF9800),
    background = Color(0xFF2E7D32),
    surface = Color(0xFFFFFFFF),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color.White,
    onSurface = Color(0xFF1C1B1F),
)

@Composable
fun PushNotiFireUTHTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}