package com.example.eksamen.ui.theme


import android.hardware.lights.Light
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color.Companion.White



private val DarkColorScheme = darkColorScheme(
    primary = Blue,
    onPrimary = TextLight,

    primaryContainer = CardGlow,
    onPrimaryContainer = TextLight,

    secondary = NeonPink,
    onSecondary = TextLight,

    tertiary = NeonYellow,
    onTertiary = TextLight,

    outline = NeonGreen,

    background = Navy,
    onBackground = TextLight,

    surface = DeepNavy,
    onSurface = TextLight,
)

@Composable
fun EksamenTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}