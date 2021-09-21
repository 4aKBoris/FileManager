package com.example.filemanager.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = LightGray,
    primaryVariant = DarkWhite,
    secondary = LightGray,
    surface = SurfaceDark,
    background = DarkBlack,
    onBackground = DarkWhite,
    secondaryVariant = SettingsColor
)

private val LightColorPalette = lightColors(
    primary = LightGray,
    primaryVariant = Black,
    secondary = LightGray,
    surface = SurfaceLight,
    background = White,
    onBackground = Black,
    secondaryVariant = SettingsColor

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun FileManagerTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) DarkColorPalette else LightColorPalette

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}