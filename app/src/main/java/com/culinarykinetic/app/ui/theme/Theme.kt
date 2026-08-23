package com.culinarykinetic.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable


private val Color2Fff = androidx.compose.ui.graphics.Color(0xFFFFFFFF)

private val AppColorScheme = lightColorScheme(
    primary = BrandOrange,
    onPrimary = Color2Fff,
    primaryContainer = BrandCreamAlt,
    onPrimaryContainer = BrandOrangeDark,
    secondary = BrandRed,
    onSecondary = Color2Fff,
    background = BrandCream,
    onBackground = CharcoalText,
    surface = CardWhite,
    onSurface = CharcoalText,
    surfaceVariant = ChipGray,
    onSurfaceVariant = SubtleGray,
    outline = DividerGray,
    error = ErrorRed
)

@Composable
fun CulinaryKineticTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AppColorScheme,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}
