package com.example.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val CyberColorScheme = darkColorScheme(
    primary = NeonCyan,
    onPrimary = CyberDark,
    primaryContainer = DeepCobalt,
    onPrimaryContainer = NeonCyan,
    secondary = ElectricBlue,
    onSecondary = TextPrimary,
    secondaryContainer = CyberDarkSurfaceVariant,
    onSecondaryContainer = TextPrimary,
    tertiary = SpeedGreen,
    onTertiary = CyberDark,
    background = CyberDark,
    onBackground = TextPrimary,
    surface = CyberDarkElevated,
    onSurface = TextPrimary,
    surfaceVariant = CyberDarkSurface,
    onSurfaceVariant = TextSecondary,
    outline = CyberBorder,
    error = AlertRed,
    onError = TextPrimary
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false, // Keep cohesive dark cyber theme
    content: @Composable () -> Unit
) {
    val colorScheme = CyberColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = CyberDark.toArgb()
                window.navigationBarColor = CyberDark.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
                WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
