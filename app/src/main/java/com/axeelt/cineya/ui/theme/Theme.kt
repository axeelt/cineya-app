package com.axeelt.cineya.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val CineYaColorScheme = darkColorScheme(

    primary = CineYaRed,

    background = CineYaBackground,

    surface = CineYaSurface,

    onPrimary = CineYaText,

    onBackground = CineYaText,

    onSurface = CineYaText
)

@Composable
fun CineYaTheme(
    content: @Composable () -> Unit
) {

    MaterialTheme(
        colorScheme = CineYaColorScheme,
        typography = Typography,
        content = content
    )
}