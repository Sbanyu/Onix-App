package com.onix.app.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.onix.app.R
import com.onix.app.ui.components.Avatar
import com.onix.app.ui.components.OnixButton
import com.onix.app.ui.components.OnixButtonVariant
import com.onix.app.ui.components.OnixCard
import com.onix.app.ui.components.SettingsGroup
import com.onix.app.ui.components.SettingsRow
import com.onix.app.ui.components.SettingsValue
import com.onix.app.ui.components.ThesisNav
import com.onix.app.ui.components.ThesisNavActions
import com.onix.app.ui.components.ThesisTab
import com.onix.app.ui.icons.OnixIcon
import com.onix.app.ui.theme.EditorialFontFamily
import com.onix.app.ui.theme.OnixColors
import com.onix.app.ui.theme.SwitzerFontFamily
import com.onix.app.ui.theme.ThesisColors

/**
 * Ported from screen_settings.jsx's HomeProfileScreen (28 · Profil): identity block, a 3-up
 * stat strip, and two SettingsGroups of read-only details and quick links.
 */
@Composable
fun HomeProfileScreen(
    navActions: ThesisNavActions,
    onOpenSettings: () -> Unit,
    onEditProfile: () -> Unit,
    onOpenHistory: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize().statusBarsPadding()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 110.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp, start = 24.dp, end = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = "Profil", color = OnixColors.Fg1, fontFamily = EditorialFontFamily, fontWeight = FontWeight.W300, fontSize = 40.sp)
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(999.dp))
                        .background(OnixColors.BgSecondary)
                        .clickable(onClick = onOpenSettings),
                    contentAlignment = Alignment.Center,
                ) {
                    OnixIcon(name = "gear", size = 20.dp, color = OnixColors.Fg1, weight = 2f)
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Box(
                        modifier = Modifier
                            .size(112.dp)
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
                    Avatar(src = R.drawable.avatar_brian, size = 96.dp, ring = true)
                }
                Text(
                    text = "Brian Wijaya",
                    color = OnixColors.Fg1,
                    fontFamily = EditorialFontFamily,
                    fontWeight = FontWeight.W300,
                    fontSize = 30.sp,
                    modifier = Modifier.padding(top = 16.dp),
                )
                Text(
                    text = "brian@onix.app",
                    color = OnixColors.Fg2,
                    fontFamily = SwitzerFontFamily,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 4.dp),
                )
                OnixButton(
                    text = "Edit Profil",
                    onClick = onEditProfile,
                    variant = OnixButtonVariant.Outline,
                    modifier = Modifier.padding(top = 14.dp),
                )
            }

            Box(modifier = Modifier.padding(top = 26.dp, start = 20.dp, end = 20.dp)) {
                OnixCard(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
                        ProfStat(value = "14", label = "Sesi", modifier = Modifier.weight(1f))
                        Box(modifier = Modifier.width(1.dp).fillMaxHeight().background(OnixColors.Border1))
                        ProfStat(value = "77", label = "Avg BPM", modifier = Modifier.weight(1f))
                        Box(modifier = Modifier.width(1.dp).fillMaxHeight().background(OnixColors.Border1))
                        ProfStat(value = "6", label = "Hari beruntun", modifier = Modifier.weight(1f))
                    }
                }
            }

            SettingsGroup(
                title = "Data Diri",
                rows = listOf(
                    { SettingsRow(icon = "user", label = "Umur", trailing = { SettingsValue("27 tahun") }) },
                    { SettingsRow(icon = "heart", label = "Berat", trailing = { SettingsValue("72 kg") }) },
                    { SettingsRow(icon = "chartBar", label = "Tinggi", trailing = { SettingsValue("178 cm") }) },
                    { SettingsRow(icon = "trophy", label = "Tujuan", trailing = { SettingsValue("Pantau kesehatan") }) },
                ),
            )

            SettingsGroup(
                title = "Aktivitas",
                rows = listOf(
                    {
                        SettingsRow(
                            icon = "list",
                            label = "Riwayat Monitoring",
                            sub = "14 sesi tersimpan",
                            onClick = onOpenHistory,
                            trailing = { OnixIcon(name = "caretRight", size = 16.dp, color = OnixColors.Fg3, weight = 2f) },
                        )
                    },
                    {
                        SettingsRow(
                            icon = "bluetooth",
                            iconColor = ThesisColors.Green,
                            label = "Perangkat",
                            sub = "ESP32_HeartMonitor",
                            onClick = onOpenSettings,
                            trailing = { OnixIcon(name = "caretRight", size = 16.dp, color = OnixColors.Fg3, weight = 2f) },
                        )
                    },
                ),
            )
        }
        ThesisNav(
            actions = navActions,
            active = ThesisTab.Profile,
            modifier = Modifier.padding(bottom = 16.dp),
        )
    }
}

@Composable
private fun ProfStat(value: String, label: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            color = OnixColors.Fg1,
            fontFamily = EditorialFontFamily,
            fontWeight = FontWeight.W300,
            fontSize = 30.sp,
            lineHeight = 30.sp,
        )
        Text(
            text = label,
            color = OnixColors.Fg3,
            fontFamily = SwitzerFontFamily,
            fontSize = 11.sp,
            modifier = Modifier.padding(top = 5.dp),
        )
    }
}
