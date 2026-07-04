package com.example.pulse_app.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors= lightColorScheme(
    primary = Indigo500,
    onPrimary = White,
    secondary = Teal400,
    background = Cloud100,
    surface = White,
    onSurface = Ink900,
    error = Danger
)

private val DarkColors= darkColorScheme(
    primary = Indigo500,
    onPrimary = White,
    secondary = Teal400,
    background = Ink900,
    surface = Ink700,
    onSurface = White,
    error = Danger
)

@Composable
fun AppTheme(
    useDark: Boolean= isSystemInDarkTheme(),content:@Composable () ->Unit
){
    MaterialTheme(
        colorScheme = if(useDark) DarkColors else LightColors,
        typography = AppTypography,
        content = content
    )
}