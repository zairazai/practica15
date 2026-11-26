package com.example.practica15.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val RedSocialColorScheme = lightColorScheme(
    primary = Rojo,
    onPrimary = Blanco,
    secondary = Negro,
    onSecondary = Blanco,
    background = Blanco,
    onBackground = Negro,
    surface = Blanco,
    onSurface = Negro
)

@Composable
fun Practica15Theme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = RedSocialColorScheme,
        typography = Typography,
        content = content
    )
}
