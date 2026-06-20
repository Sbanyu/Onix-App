package com.onix.app.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.onix.app.ui.icons.OnixIcon
import com.onix.app.ui.theme.OnixColors
import com.onix.app.ui.theme.OnixRadius
import com.onix.app.ui.theme.SwitzerFontFamily
import com.onix.app.ui.theme.insetTopGlow

/**
 * Shared atoms ported from screen_settings.jsx's SpToggle/SpRow/SpValue/SpGroup, reused across
 * the Pengaturan, Profil, Privasi & Keamanan, and Tentang Onix screens.
 */
@Composable
fun SettingsToggle(on: Boolean, onToggle: () -> Unit, modifier: Modifier = Modifier) {
    val thumbOffset by animateDpAsState(targetValue = if (on) 23.dp else 3.dp, label = "settingsToggleThumb")
    Box(
        modifier = modifier
            .size(width = 50.dp, height = 30.dp)
            .clip(RoundedCornerShape(OnixRadius.full))
            .background(if (on) OnixColors.Red else OnixColors.BgTertiary)
            .let { if (on) it.insetTopGlow(OnixColors.RedGlow, height = 3.dp) else it }
            .clickable(onClick = onToggle),
    ) {
        Box(
            modifier = Modifier
                .padding(start = thumbOffset, top = 3.dp)
                .size(24.dp)
                .clip(CircleShape)
                .background(OnixColors.White),
        )
    }
}

@Composable
fun SettingsRow(
    icon: String,
    label: String,
    modifier: Modifier = Modifier,
    iconColor: Color? = null,
    sub: String? = null,
    danger: Boolean = false,
    onClick: (() -> Unit)? = null,
    trailing: @Composable () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .let { if (onClick != null) it.clickable(onClick = onClick) else it }
            .padding(vertical = 13.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Box(
            modifier = Modifier.size(38.dp).clip(RoundedCornerShape(11.dp)).background(OnixColors.BgTertiary),
            contentAlignment = Alignment.Center,
        ) {
            OnixIcon(name = icon, size = 18.dp, color = if (danger) OnixColors.Red else (iconColor ?: OnixColors.Fg1), weight = 2f)
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                color = if (danger) OnixColors.Red else OnixColors.Fg1,
                fontFamily = SwitzerFontFamily,
                fontWeight = FontWeight.W500,
                fontSize = 15.sp,
            )
            if (sub != null) {
                Text(
                    text = sub,
                    color = OnixColors.Fg3,
                    fontFamily = SwitzerFontFamily,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 2.dp),
                )
            }
        }
        trailing()
    }
}

@Composable
fun SettingsValue(text: String, modifier: Modifier = Modifier) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(text = text, color = OnixColors.Fg3, fontFamily = SwitzerFontFamily, fontSize = 13.sp)
        OnixIcon(name = "caretRight", size = 15.dp, color = OnixColors.Fg3, weight = 2f)
    }
}

@Composable
fun SettingsGroup(rows: List<@Composable () -> Unit>, modifier: Modifier = Modifier, title: String? = null) {
    Column(modifier = modifier.fillMaxWidth().padding(top = 22.dp, start = 20.dp, end = 20.dp)) {
        if (title != null) {
            Text(
                text = title.uppercase(),
                color = OnixColors.Fg3,
                fontFamily = SwitzerFontFamily,
                fontSize = 11.sp,
                letterSpacing = 1.32.sp,
                modifier = Modifier.padding(start = 4.dp, end = 4.dp, bottom = 8.dp),
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(OnixColors.BgSecondary)
                .padding(vertical = 2.dp, horizontal = 12.dp),
        ) {
            rows.forEachIndexed { index, row ->
                row()
                if (index < rows.lastIndex) {
                    Box(modifier = Modifier.fillMaxWidth().padding(start = 52.dp).height(1.dp).background(OnixColors.Border1))
                }
            }
        }
    }
}
