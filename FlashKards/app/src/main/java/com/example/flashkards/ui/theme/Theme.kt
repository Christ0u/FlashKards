package com.example.flashkards.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

// Palette de couleurs pour le mode sombre
private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

// Palette de couleurs pour le mode clair
private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

// Composable qui applique le thème à l'application
@Composable
fun FlashKards(
    darkTheme: Boolean = isSystemInDarkTheme(), // Utilise le thème sombre du système par défaut
    dynamicColor: Boolean = true, // Active les couleurs dynamiques sur Android 12+
    content: @Composable () -> Unit
) {
    // Sélectionne la palette de couleurs en fonction du thème et de la version Android
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // Applique le thème Material 3 à l'application
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}