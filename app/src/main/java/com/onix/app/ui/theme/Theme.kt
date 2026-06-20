package com.onix.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/** Onix ships dark-first only; there is no light theme in the supported scope. */
private val OnixDarkColorScheme = darkColorScheme(
    primary = OnixColors.Red,
    onPrimary = OnixColors.White,
    background = OnixColors.BgPrimary,
    onBackground = OnixColors.Fg1,
    surface = OnixColors.BgSecondary,
    onSurface = OnixColors.Fg1,
    surfaceVariant = OnixColors.BgTertiary,
    error = OnixColors.Error,
)

@Composable
fun OnixTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = OnixDarkColorScheme,
        typography = MaterialTheme.typography,
        content = content,
    )
}

/** Top radial warm-red glow used behind the phone canvas on most screens (kit.jsx PhoneFrame). */
val OnixGlowTop = Color(0x8CEF4444) // rgba(239,68,68,.55) approx peak
