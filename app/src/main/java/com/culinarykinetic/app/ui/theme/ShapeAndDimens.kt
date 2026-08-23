package com.culinarykinetic.app.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(6.dp),
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(22.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

object Dimens {
    val ScreenPadding = 20.dp
    val CardPadding = 16.dp
    val CardCorner = 18.dp
    val ChipCorner = 100.dp
    val ButtonCorner = 100.dp
    val SpaceXXS = 4.dp
    val SpaceXS = 8.dp
    val SpaceS = 12.dp
    val SpaceM = 16.dp
    val SpaceL = 24.dp
    val SpaceXL = 32.dp
    val IconButtonSize = 40.dp
}
