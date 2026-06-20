package com.onix.app.ui.theme

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Approximates CSS `box-shadow: inset 0 Npx Npx <color>` (the warm highlight along the top
 * edge of red surfaces — primary buttons, the FAB, LogoDisc) with a fading gradient drawn
 * over the content, clipped by whatever shape the caller already applied upstream in the chain.
 */
fun Modifier.insetTopGlow(color: Color, height: Dp = 10.dp): Modifier = drawWithContent {
    drawContent()
    val h = height.toPx()
    drawRect(
        brush = Brush.verticalGradient(
            colors = listOf(color, color.copy(alpha = 0f)),
            startY = 0f,
            endY = h,
        ),
        size = Size(size.width, h),
    )
}
