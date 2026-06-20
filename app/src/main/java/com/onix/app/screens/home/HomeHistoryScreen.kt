package com.onix.app.screens.home

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.onix.app.ui.components.OnixCard
import com.onix.app.ui.components.StatusPill
import com.onix.app.ui.components.ThesisNav
import com.onix.app.ui.components.ThesisNavActions
import com.onix.app.ui.components.ThesisTab
import com.onix.app.ui.icons.OnixIcon
import com.onix.app.ui.theme.EditorialFontFamily
import com.onix.app.ui.theme.OnixColors
import com.onix.app.ui.theme.SwitzerFontFamily
import com.onix.app.ui.theme.ThesisColors

private data class HistorySession(val time: String, val bpm: Int, val dur: String, val abnormal: Boolean)
private data class HistoryDayGroup(val day: String, val date: String?, val sessions: List<HistorySession>)

private val historyDays = listOf(
    HistoryDayGroup(
        day = "Hari Ini",
        date = "Kamis, 26 Jun",
        sessions = listOf(
            HistorySession(time = "09:41", bpm = 76, dur = "08:42", abnormal = false),
            HistorySession(time = "07:15", bpm = 72, dur = "05:10", abnormal = false),
        ),
    ),
    HistoryDayGroup(
        day = "Kemarin",
        date = "Rabu, 25 Jun",
        sessions = listOf(
            HistorySession(time = "21:30", bpm = 88, dur = "12:04", abnormal = true),
            HistorySession(time = "08:02", bpm = 74, dur = "06:33", abnormal = false),
        ),
    ),
    HistoryDayGroup(
        day = "Senin, 23 Jun",
        date = null,
        sessions = listOf(
            HistorySession(time = "19:48", bpm = 79, dur = "09:55", abnormal = false),
        ),
    ),
)

/**
 * Ported from screen_home_thesis.jsx's HomeHistoryScreen (26 · Riwayat — Activity History):
 * a 7-day summary card followed by sessions grouped by day. The header's calendar button is
 * decorative in the source (no `onClick`), so it's left non-interactive here too.
 */
@Composable
fun HomeHistoryScreen(navActions: ThesisNavActions, modifier: Modifier = Modifier) {
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
                Text(text = "Riwayat", color = OnixColors.Fg1, fontFamily = EditorialFontFamily, fontWeight = FontWeight.W300, fontSize = 40.sp)
                Box(
                    modifier = Modifier.size(44.dp).clip(RoundedCornerShape(999.dp)).background(OnixColors.BgSecondary),
                    contentAlignment = Alignment.Center,
                ) {
                    OnixIcon(name = "calendar", size = 20.dp, color = OnixColors.Fg1, weight = 2f)
                }
            }

            Box(modifier = Modifier.padding(top = 18.dp, start = 20.dp, end = 20.dp)) {
                OnixCard(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Ringkasan 7 Hari".uppercase(),
                        color = OnixColors.Fg3,
                        fontFamily = SwitzerFontFamily,
                        fontSize = 11.sp,
                        letterSpacing = 1.32.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 14.dp),
                    )
                    Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
                        HistorySummary(label = "Sesi", value = "14", modifier = Modifier.weight(1f))
                        Box(modifier = Modifier.width(1.dp).fillMaxHeight().background(OnixColors.Border1))
                        HistorySummary(label = "Avg", value = "77", unit = "BPM", modifier = Modifier.weight(1f))
                        Box(modifier = Modifier.width(1.dp).fillMaxHeight().background(OnixColors.Border1))
                        HistorySummary(label = "Abnormal", value = "1", modifier = Modifier.weight(1f))
                    }
                }
            }

            historyDays.forEach { group ->
                Column(modifier = Modifier.padding(top = 22.dp, start = 20.dp, end = 20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(start = 4.dp, end = 4.dp, bottom = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom,
                    ) {
                        Text(text = group.day, color = OnixColors.Fg1, fontFamily = SwitzerFontFamily, fontWeight = FontWeight.W600, fontSize = 14.sp)
                        if (group.date != null) {
                            Text(text = group.date, color = OnixColors.Fg3, fontFamily = SwitzerFontFamily, fontSize = 12.sp)
                        }
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        group.sessions.forEach { session ->
                            HistoryRow(time = session.time, bpm = session.bpm, dur = session.dur, abnormal = session.abnormal)
                        }
                    }
                }
            }
        }
        ThesisNav(
            actions = navActions,
            active = ThesisTab.Riwayat,
            modifier = Modifier.padding(bottom = 16.dp),
        )
    }
}

@Composable
private fun HistoryRow(time: String, bpm: Int, dur: String, abnormal: Boolean, modifier: Modifier = Modifier) {
    val color = if (abnormal) ThesisColors.Amber else ThesisColors.Green
    val colorBg = if (abnormal) ThesisColors.AmberBg else ThesisColors.GreenBg

    OnixCard(modifier = modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            Box(
                modifier = Modifier.size(42.dp).clip(RoundedCornerShape(13.dp)).background(OnixColors.BgTertiary),
                contentAlignment = Alignment.Center,
            ) {
                OnixIcon(name = "heart", size = 19.dp, color = if (abnormal) ThesisColors.Amber else OnixColors.Red, weight = 2f)
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(text = time, color = OnixColors.Fg1, fontFamily = SwitzerFontFamily, fontWeight = FontWeight.W600, fontSize = 15.sp)
                Text(
                    text = "Durasi $dur",
                    color = OnixColors.Fg3,
                    fontFamily = SwitzerFontFamily,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 2.dp),
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "$bpm",
                        color = OnixColors.Fg1,
                        fontFamily = EditorialFontFamily,
                        fontWeight = FontWeight.W300,
                        fontSize = 26.sp,
                        lineHeight = 26.sp,
                    )
                    Text(text = " BPM", color = OnixColors.Fg3, fontFamily = SwitzerFontFamily, fontSize = 11.sp)
                }
                StatusPill(
                    text = if (abnormal) "Abnormal" else "Normal",
                    color = color,
                    background = colorBg,
                    leading = {},
                    modifier = Modifier.padding(top = 6.dp),
                )
            }
        }
    }
}

@Composable
private fun HistorySummary(label: String, value: String, unit: String? = null, modifier: Modifier = Modifier) {
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
            text = if (unit != null) "$label · $unit" else label,
            color = OnixColors.Fg3,
            fontFamily = SwitzerFontFamily,
            fontSize = 11.sp,
            modifier = Modifier.padding(top = 5.dp),
        )
    }
}
