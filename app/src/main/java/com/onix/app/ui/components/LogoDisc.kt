package com.onix.app.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.onix.app.ui.theme.OnixColors
import com.onix.app.ui.theme.insetTopGlow

/**
 * Ported from kit.jsx's LogoDisc: a rounded-square housing the pinwheel mark, filled with the
 * radial gradient `radial-gradient(120% 120% at 50% 0%, #F87171, #EF4444 55%, #DC2626)`.
 */
@Composable
fun LogoDisc(size: Dp = 64.dp, radius: Dp = 18.dp, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(radius))
            .drawWithCache {
                val brush = Brush.radialGradient(
                    0f to OnixColors.RedSoft,
                    0.55f to OnixColors.Red,
                    1f to OnixColors.RedHover,
                    center = Offset(this.size.width / 2f, 0f),
                    radius = this.size.width * 1.2f,
                )
                onDrawBehind { drawRect(brush) }
            }
            .insetTopGlow(OnixColors.RedGlow, height = size / 4),
        contentAlignment = Alignment.Center,
    ) {
        PinwheelMark(size = size * 0.62f, color = Color.White)
    }
}
