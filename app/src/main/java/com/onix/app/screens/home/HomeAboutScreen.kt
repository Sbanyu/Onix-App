package com.onix.app.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.onix.app.ui.components.LogoDisc
import com.onix.app.ui.components.SettingsGroup
import com.onix.app.ui.components.SubPage
import com.onix.app.ui.icons.OnixIcon
import com.onix.app.ui.theme.EditorialFontFamily
import com.onix.app.ui.theme.OnixColors
import com.onix.app.ui.theme.SwitzerFontFamily

/**
 * Ported from screen_settings.jsx's HomeAboutScreen (31 · Tentang Onix): a brand block over
 * two SettingsGroups of links and a centered copyright footer. Only "Kebijakan Privasi" has
 * an onClick in the source; the rest are non-interactive here too.
 */
@Composable
fun HomeAboutScreen(onBack: () -> Unit, onOpenPrivacy: () -> Unit, modifier: Modifier = Modifier) {
    SubPage(title = "Tentang Onix", onBack = onBack, modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .drawWithCache {
                            val brush = Brush.radialGradient(
                                0f to OnixColors.Red.copy(alpha = 0.35f),
                                0.7f to OnixColors.Red.copy(alpha = 0f),
                                center = Offset(this.size.width / 2f, this.size.height / 2f),
                                radius = this.size.width / 2f,
                            )
                            onDrawBehind { drawRect(brush) }
                        },
                )
                LogoDisc(size = 84.dp, radius = 24.dp)
            }
            Text(
                text = "Onix",
                color = OnixColors.Fg1,
                fontFamily = EditorialFontFamily,
                fontWeight = FontWeight.W300,
                fontSize = 34.sp,
                modifier = Modifier.padding(top = 22.dp),
            )
            Text(
                text = "Versi 1.0.0 (build 26)",
                color = OnixColors.Fg3,
                fontFamily = SwitzerFontFamily,
                fontSize = 13.sp,
                modifier = Modifier.padding(top = 4.dp),
            )
            Text(
                text = "Monitoring detak jantung real-time dengan ESP32 & sensor MAX30102, difilter menggunakan Moving Average.",
                color = OnixColors.Fg2,
                fontFamily = SwitzerFontFamily,
                fontSize = 14.sp,
                lineHeight = 21.7.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 14.dp).width(300.dp),
            )
        }

        SettingsGroup(
            title = "Informasi",
            rows = listOf(
                { AboutLink(label = "Apa itu PPG?") },
                { AboutLink(label = "Cara kerja Moving Average") },
                { AboutLink(label = "Spesifikasi Perangkat") },
            ),
        )

        SettingsGroup(
            title = "Dukungan",
            rows = listOf(
                { AboutLink(label = "Pusat Bantuan") },
                { AboutLink(label = "Hubungi Kami") },
                { AboutLink(label = "Syarat & Ketentuan") },
                { AboutLink(label = "Kebijakan Privasi", onClick = onOpenPrivacy) },
            ),
        )

        Column(
            modifier = Modifier.fillMaxWidth().padding(top = 24.dp, start = 28.dp, end = 28.dp, bottom = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = "Dibuat untuk penelitian skripsi", color = OnixColors.Fg3, fontFamily = SwitzerFontFamily, fontSize = 12.sp)
            Text(
                text = "© 2025 Onix · All rights reserved",
                color = OnixColors.Fg3,
                fontFamily = SwitzerFontFamily,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp),
            )
        }
    }
}

@Composable
private fun AboutLink(label: String, modifier: Modifier = Modifier, onClick: (() -> Unit)? = null) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .let { if (onClick != null) it.clickable(onClick = onClick) else it }
            .padding(vertical = 14.dp, horizontal = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = label, color = OnixColors.Fg1, fontFamily = SwitzerFontFamily, fontSize = 15.sp)
        OnixIcon(name = "caretRight", size = 16.dp, color = OnixColors.Fg3, weight = 2f)
    }
}
