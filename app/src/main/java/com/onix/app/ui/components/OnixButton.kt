package com.onix.app.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.onix.app.ui.icons.OnixIcon
import com.onix.app.ui.theme.OnixColors
import com.onix.app.ui.theme.OnixRadius
import com.onix.app.ui.theme.OnixSpacing
import com.onix.app.ui.theme.SwitzerFontFamily
import com.onix.app.ui.theme.insetTopGlow

enum class OnixButtonVariant { Primary, Secondary, Outline, Ghost }

/** Ported from kit.jsx's Button: pill CTA with a press-scale and (primary only) the inset red glow. */
@Composable
fun OnixButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: OnixButtonVariant = OnixButtonVariant.Primary,
    icon: String? = null,
    fullWidth: Boolean = false,
    enabled: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (pressed) 0.97f else 1f, label = "onixButtonScale")

    val background = when (variant) {
        OnixButtonVariant.Primary -> if (pressed) OnixColors.RedPress else OnixColors.Red
        OnixButtonVariant.Secondary -> OnixColors.BgSecondary
        OnixButtonVariant.Outline, OnixButtonVariant.Ghost -> Color.Transparent
    }
    val contentColor = when (variant) {
        OnixButtonVariant.Primary -> OnixColors.White
        OnixButtonVariant.Secondary, OnixButtonVariant.Outline -> OnixColors.Fg1
        OnixButtonVariant.Ghost -> OnixColors.Fg2
    }

    var rowModifier = modifier
        .let { if (fullWidth) it.fillMaxWidth() else it }
        .graphicsLayer { scaleX = scale; scaleY = scale }
        .clip(RoundedCornerShape(OnixRadius.full))
    if (variant == OnixButtonVariant.Outline) {
        rowModifier = rowModifier.border(1.dp, OnixColors.Border2, RoundedCornerShape(OnixRadius.full))
    }
    rowModifier = rowModifier.background(background)
    if (variant == OnixButtonVariant.Primary) {
        rowModifier = rowModifier.insetTopGlow(OnixColors.RedGlow, height = 10.dp)
    }
    rowModifier = rowModifier
        .clickable(
            interactionSource = interactionSource,
            indication = null,
            enabled = enabled,
            onClick = onClick,
        )
        .padding(vertical = OnixSpacing.base16, horizontal = OnixSpacing.xl28)

    Row(
        modifier = rowModifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(9.dp, Alignment.CenterHorizontally),
    ) {
        if (icon != null) {
            OnixIcon(name = icon, size = 18.dp, color = contentColor, weight = 2f)
        }
        Text(
            text = text,
            color = contentColor,
            fontFamily = SwitzerFontFamily,
            fontWeight = FontWeight.W600,
            fontSize = 16.sp,
        )
    }
}
