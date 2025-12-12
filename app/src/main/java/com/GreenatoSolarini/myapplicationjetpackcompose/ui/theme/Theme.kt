package com.greenatosolarini.myapplicationjetpackcompose.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = GreenLight,
    onPrimary = GreenDark,
    secondary = GreenPrimary,
    onSecondary = White,
    tertiary = YellowEnergy,
    background = Color.Black,
    onBackground = White,
    surface = Color.Black,
    onSurface = White
)

private val LightColorScheme = lightColorScheme(
    primary = GreenPrimary,
    onPrimary = White,
    primaryContainer = GreenLight,
    onPrimaryContainer = GreenDark,

    secondary = GreenDark,
    onSecondary = White,
    secondaryContainer = GreenLight,
    onSecondaryContainer = GreenDark,

    tertiary = YellowEnergy,
    onTertiary = GreenDark,
    tertiaryContainer = YellowEnergy,
    onTertiaryContainer = GreenDark,

    background = GrayLight,
    onBackground = GreenDark,

    surface = White,
    onSurface = GreenDark,
    surfaceVariant = Color(0xFFE4E4E4),
    onSurfaceVariant = GreenDark
)




@Composable
fun MyApplicationJetpackComposeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,  // ðŸ‘ˆ antes tenÃ­as "colors"
        typography = Typography,
        content = content
    )
}

