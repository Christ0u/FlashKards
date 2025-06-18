package com.example.flashkards.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Définit la configuration de la typographie Material utilisée dans l'application
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,      // Police par défaut
        fontWeight = FontWeight.Normal,       // Poids normal
        fontSize = 16.sp,                     // Taille de police
        lineHeight = 24.sp,                   // Hauteur de ligne
        letterSpacing = 0.5.sp                // Espacement des lettres
    )
)