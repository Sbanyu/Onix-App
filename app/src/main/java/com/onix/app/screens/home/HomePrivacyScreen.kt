package com.onix.app.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.onix.app.ui.components.OnixCard
import com.onix.app.ui.components.SettingsGroup
import com.onix.app.ui.components.SettingsRow
import com.onix.app.ui.components.SettingsToggle
import com.onix.app.ui.components.SubPage
import com.onix.app.ui.icons.OnixIcon
import com.onix.app.ui.theme.OnixColors
import com.onix.app.ui.theme.SwitzerFontFamily
import com.onix.app.ui.theme.ThesisColors

/**
 * Ported from screen_settings.jsx's HomePrivacyScreen (30 · Privasi & Keamanan): a hero
 * explainer card over three SettingsGroups of toggles and data actions. The "Kebijakan
 * Privasi" line and "Ubah Kata Sandi"/"Ekspor Data"/"Hapus Semua Data" rows have no onClick
 * in the source, so they're left non-interactive here too.
 */
@Composable
fun HomePrivacyScreen(onBack: () -> Unit, modifier: Modifier = Modifier) {
    var share by remember { mutableStateOf(false) }
    var analytics by remember { mutableStateOf(true) }
    var biometric by remember { mutableStateOf(true) }

    SubPage(title = "Privasi & Keamanan", onBack = onBack, modifier = modifier) {
        Box(modifier = Modifier.fillMaxWidth().padding(top = 8.dp, start = 20.dp, end = 20.dp)) {
            OnixCard(modifier = Modifier.fillMaxWidth()) {
                Row(horizontalArrangement = Arrangement.spacedBy(13.dp), verticalAlignment = Alignment.Top) {
                    Box(
                        modifier = Modifier.size(40.dp).clip(RoundedCornerShape(12.dp)).background(ThesisColors.GreenBg),
                        contentAlignment = Alignment.Center,
                    ) {
                        OnixIcon(name = "shield", size = 18.dp, color = ThesisColors.Green, weight = 2f)
                    }
                    Text(
                        text = "Data detak jantung kamu disimpan terenkripsi di perangkat. Onix tidak membagikannya tanpa izinmu.",
                        color = OnixColors.Fg2,
                        fontFamily = SwitzerFontFamily,
                        fontSize = 13.sp,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }

        SettingsGroup(
            title = "Data",
            rows = listOf(
                {
                    SettingsRow(
                        icon = "share",
                        label = "Bagikan data anonim",
                        sub = "Untuk riset kesehatan",
                        trailing = { SettingsToggle(on = share, onToggle = { share = !share }) },
                    )
                },
                {
                    SettingsRow(
                        icon = "chartBar",
                        label = "Analitik penggunaan",
                        sub = "Bantu tingkatkan aplikasi",
                        trailing = { SettingsToggle(on = analytics, onToggle = { analytics = !analytics }) },
                    )
                },
            ),
        )

        SettingsGroup(
            title = "Keamanan",
            rows = listOf(
                {
                    SettingsRow(
                        icon = "lock",
                        label = "Buka dengan biometrik",
                        sub = "Face ID / sidik jari",
                        trailing = { SettingsToggle(on = biometric, onToggle = { biometric = !biometric }) },
                    )
                },
                {
                    SettingsRow(
                        icon = "lock",
                        label = "Ubah Kata Sandi",
                        trailing = { OnixIcon(name = "caretRight", size = 16.dp, color = OnixColors.Fg3, weight = 2f) },
                    )
                },
            ),
        )

        SettingsGroup(
            title = "Data Saya",
            rows = listOf(
                {
                    SettingsRow(
                        icon = "list",
                        label = "Ekspor Data",
                        sub = "Unduh riwayat sebagai CSV",
                        trailing = { OnixIcon(name = "caretRight", size = 16.dp, color = OnixColors.Fg3, weight = 2f) },
                    )
                },
                {
                    SettingsRow(
                        icon = "warning",
                        label = "Hapus Semua Data",
                        danger = true,
                        trailing = { OnixIcon(name = "caretRight", size = 16.dp, color = OnixColors.Fg3, weight = 2f) },
                    )
                },
            ),
        )

        Text(
            text = "Kebijakan Privasi",
            color = OnixColors.Red,
            fontFamily = SwitzerFontFamily,
            fontWeight = FontWeight.W600,
            fontSize = 13.sp,
            modifier = Modifier.fillMaxWidth().padding(top = 22.dp),
            textAlign = TextAlign.Center,
        )
    }
}
