package com.example.curtineat.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val WhiteColorScheme = lightColorScheme(
    primary = WhitePrimary,
    onPrimary = WhiteOnPrimary,
    primaryContainer = WhitePrimaryContainer,
    onPrimaryContainer = WhiteOnPrimaryContainer,
    secondary = WhiteSecondary,
    onSecondary = WhiteOnPrimary,
    secondaryContainer = WhiteSecondaryContainer,
    onSecondaryContainer = WhiteOnSecondaryContainer,
    tertiary = WhiteTertiary,
    onTertiary = WhiteOnTertiary,
    background = WhiteBackground,
    surface = WhiteSurface,
    surfaceVariant = WhiteSurfaceVariant,
    onSurface = WhiteOnSurface,
    onSurfaceVariant = WhiteOnSurfaceVariant,
    outline = WhiteOutline,
    error = WhiteError,
    onError = WhiteOnError,
    surfaceContainerLow = WhiteSurface,
    surfaceContainer = WhiteSurfaceVariant,
    outlineVariant = WhiteOutline
)

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkOnPrimary,
    primaryContainer = DarkPrimaryContainer,
    onPrimaryContainer = DarkOnPrimaryContainer,
    secondary = DarkSecondary,
    onSecondary = DarkOnPrimary,
    secondaryContainer = DarkSecondaryContainer,
    onSecondaryContainer = DarkOnSecondaryContainer,
    tertiary = DarkTertiary,
    onTertiary = DarkOnTertiary,
    background = DarkBackground,
    surface = DarkSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurface = DarkOnSurface,
    onSurfaceVariant = DarkOnSurfaceVariant,
    outline = DarkOutline,
    error = DarkError,
    onError = DarkOnError,
    surfaceContainerLow = DarkSurface,
    surfaceContainer = DarkSurfaceVariant,
    outlineVariant = DarkOutline
)

private val WarmColorScheme = lightColorScheme(
    primary = WarmPrimary,
    onPrimary = WarmOnPrimary,
    primaryContainer = WarmPrimaryContainer,
    onPrimaryContainer = WarmOnPrimaryContainer,
    secondary = WarmSecondary,
    onSecondary = WarmOnPrimary,
    secondaryContainer = WarmSecondaryContainer,
    onSecondaryContainer = WarmOnSecondaryContainer,
    tertiary = WarmTertiary,
    onTertiary = WarmOnTertiary,
    background = WarmBackground,
    surface = WarmSurface,
    surfaceVariant = WarmSurfaceVariant,
    onSurface = WarmOnSurface,
    onSurfaceVariant = WarmOnSurfaceVariant,
    outline = WarmOutline,
    error = WarmError,
    onError = WarmOnError,
    surfaceContainerLow = WarmSurface,
    surfaceContainer = WarmSurfaceVariant,
    outlineVariant = WarmOutline
)

private val FreshColorScheme = lightColorScheme(
    primary = FreshPrimary,
    onPrimary = FreshOnPrimary,
    primaryContainer = FreshPrimaryContainer,
    onPrimaryContainer = FreshOnPrimaryContainer,
    secondary = FreshSecondary,
    onSecondary = FreshOnPrimary,
    secondaryContainer = FreshSecondaryContainer,
    onSecondaryContainer = FreshOnSecondaryContainer,
    tertiary = FreshTertiary,
    onTertiary = FreshOnTertiary,
    background = FreshBackground,
    surface = FreshSurface,
    surfaceVariant = FreshSurfaceVariant,
    onSurface = FreshOnSurface,
    onSurfaceVariant = FreshOnSurfaceVariant,
    outline = FreshOutline,
    error = FreshError,
    onError = FreshOnError,
    surfaceContainerLow = FreshSurface,
    surfaceContainer = FreshSurfaceVariant,
    outlineVariant = FreshOutline
)

@Composable
fun CurtinEATTheme(
    themeMode: AppThemeMode = AppThemeMode.SYSTEM,
    content: @Composable () -> Unit
) {
    val systemIsDark = isSystemInDarkTheme()

    val colorScheme = when (themeMode) {
        AppThemeMode.SYSTEM ->
            if (systemIsDark) DarkColorScheme else WhiteColorScheme

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