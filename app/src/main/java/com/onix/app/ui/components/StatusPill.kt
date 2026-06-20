package com.onix.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.onix.app.ui.theme.OnixRadius
import com.onix.app.ui.theme.SwitzerFontFamily

/**
 * Small rounded status chip used throughout the thesis screens (NORMAL/abnormal badges,
 * connection chips, signal indicators). Ported from the repeated inline `<span>` pill style
 * in screen_home_thesis.jsx. Defaults to a colored leading dot; pass [leading] to swap it for
 * something else (e.g. [com.onix.app.ui.icons.OnixIcon] signal bars) or `{}` to omit it.
 */
@Composable
fun StatusPill(
    text: String,
    color: Color,
    background: Color,
    modifier: Modifier = Modifier,
    bordered: Boolean = true,
    leading: @Composable () -> Unit = { Box(Modifier.size(6.dp).clip(CircleShape).background(color)) },
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(OnixRadius.full))
            .background(background)
            .let { if (bordered) it.border(1.dp, color, RoundedCornerShape(OnixRadius.full)) else it }
            .padding(vertical = 5.dp, horizontal = 11.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        leading()
        Text(text = text, color = color, fontFamily = SwitzerFontFamily, fontWeight = FontWeight.W600, fontSize = 11.sp)
    }
}
