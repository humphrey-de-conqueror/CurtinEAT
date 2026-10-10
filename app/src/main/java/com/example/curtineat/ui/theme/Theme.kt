package com.example.curtineat.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val WhiteColorScheme = lightColorScheme(
    primary = WhitePrimary,
    onPrimary = WhiteOnPrimary,
    secondary = WhiteSecondary,
    background = WhiteBackground,
    surface = WhiteSurface,
    surfaceVariant = WhiteSurfaceVariant,
    onSurface = WhiteOnSurface,
    onSurfaceVariant = WhiteOnSurfaceVariant,
    outline = WhiteOutline
)

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkOnPrimary,
    secondary = DarkSecondary,
    background = DarkBackground,
    surface = DarkSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurface = DarkOnSurface,
    onSurfaceVariant = DarkOnSurfaceVariant,
    outline = DarkOutline
)

private val WarmColorScheme = lightColorScheme(
    primary = WarmPrimary,
    onPrimary = WarmOnPrimary,
    secondary = WarmSecondary,
    background = WarmBackground,
    surface = WarmSurface,
    surfaceVariant = WarmSurfaceVariant,
    onSurface = WarmOnSurface,
    onSurfaceVariant = WarmOnSurfaceVariant,
    outline = WarmOutline
)

private val FreshColorScheme = lightColorScheme(
    primary = FreshPrimary,
    onPrimary = FreshOnPrimary,
    secondary = FreshSecondary,
    background = FreshBackground,
    surface = FreshSurface,
    surfaceVariant = FreshSurfaceVariant,
    onSurface = FreshOnSurface,
    onSurfaceVariant = FreshOnSurfaceVariant,
    outline = FreshOutline
)

@Composable
fun CurtinEATTheme(
    themeMode: AppThemeMode = AppThemeMode.SYSTEM,
    content: @Composable () -> Unit
) {
    val systemIsDark = isSystemInDarkTheme()

    val colorScheme = when (themeMode) {
        AppThemeMode.SYSTEM -> {
            if (systemIsDark) {
                DarkColorScheme
            } else {
                WhiteColorScheme
            }
        }

        AppThemeMode.DARK -> DarkColorScheme
        AppThemeMode.WHITE -> WhiteColorScheme
        AppThemeMode.WARM -> WarmColorScheme
        AppThemeMode.FRESH -> FreshColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}