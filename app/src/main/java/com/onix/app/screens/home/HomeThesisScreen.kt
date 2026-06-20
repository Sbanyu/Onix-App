package com.onix.app.screens.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.onix.app.R
import com.onix.app.ui.components.Avatar
import com.onix.app.ui.components.OnixCard
import com.onix.app.ui.components.StatusPill
import com.onix.app.ui.components.ThesisFabMode
import com.onix.app.ui.components.ThesisNav
import com.onix.app.ui.components.ThesisNavActions
import com.onix.app.ui.components.ThesisTab
import com.onix.app.ui.icons.OnixIcon
import com.onix.app.ui.theme.EditorialFontFamily
import com.onix.app.ui.theme.OnixColors
import com.onix.app.ui.theme.OnixRadius
import com.onix.app.ui.theme.SwitzerFontFamily
import com.onix.app.ui.theme.ThesisColors
import com.onix.app.ui.theme.insetTopGlow
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin

/**
 * Ported from screen_home_thesis.jsx's HomeThesisScreen (22 · Home — Thesis / 23 · Home — No
 * Device, same composable toggled by [connected]). Weather is dropped, same as the source.
 */
@Composable
fun HomeThesisScreen(
    connected: Boolean,
    navActions: ThesisNavActions,
    onConnectDevice: () -> Unit,
    onSeeAllTrend: () -> Unit,
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
                verticalAlignment = Alignment.Top,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Morning,\nBrian",
                        color = OnixColors.Fg1,
                        fontFamily = EditorialFontFamily,
                        fontWeight = FontWeight.W300,
                        fontSize = 46.sp,
                        lineHeight = 50.sp,
                    )
                    if (connected) {
                        Row(
                            modifier = Modifier.padding(top = 14.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            StatusPill(text = "ESP32 Terhubung ✓", color = ThesisColors.Green, background = ThesisColors.GreenBg)
                            StatusPill(
                                text = "Sinyal Baik",
                                color = ThesisColors.Green,
                                background = ThesisColors.GreenBg,
                                leading = { SignalBars(level = 3) },
                            )
                        }
                    }
                }
                Avatar(src = R.drawable.avatar_brian, size = 48.dp)
            }

            if (!connected) {
                Box(modifier = Modifier.padding(top = 18.dp, start = 20.dp, end = 20.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(OnixRadius.xl2_16))
                            .background(ThesisColors.AmberBg)
                            .padding(vertical = 14.dp, horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        OnixIcon(name = "warning", size = 20.dp, color = ThesisColors.Amber, weight = 2f)
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Perangkat tidak terhubung",
                                color = ThesisColors.Amber,
                                fontFamily = SwitzerFontFamily,
                                fontWeight = FontWeight.W600,
                                fontSize = 13.5.sp,
                            )
                            Text(
                                text = "Hubungkan ESP32 untuk mulai monitoring",
                                color = OnixColors.Fg2,
                                fontFamily = SwitzerFontFamily,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(top = 2.dp),
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(OnixRadius.full))
                                .background(ThesisColors.Amber)
                                .clickable(onClick = onConnectDevice)
                                .padding(horizontal = 14.dp, vertical = 7.dp),
                        ) {
                            Text(
                                text = "Hubungkan",
                                color = Color.White,
                                fontFamily = SwitzerFontFamily,
                                fontWeight = FontWeight.W600,
                                fontSize = 11.sp,
                            )
                        }
                    }
                }
            }

            Box(modifier = Modifier.fillMaxWidth().padding(top = if (connected) 22.dp else 16.dp)) {
                BpmGauge(value = 76, pct = 0.62f, empty = !connected, modifier = Modifier.align(Alignment.Center))
            }

            Box(modifier = Modifier.padding(top = 36.dp, start = 20.dp, end = 20.dp)) {
                ConditionCard(empty = !connected)
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp, start = 20.dp, end = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                HtStatCard(
                    icon = "heart",
                    iconColor = OnixColors.Red,
                    label = "Rata-rata BPM",
                    value = if (connected) "76" else "—",
                    unit = if (connected) "BPM" else null,
                    sub = if (connected) null else "belum ada data",
                    disabled = !connected,
                    modifier = Modifier.weight(1f),
                )
                HtStatCard(
                    icon = "clock",
                    iconColor = OnixColors.Fg2,
                    label = "Durasi Sesi",
                    value = if (connected) "08:42" else "—",
                    unit = null,
                    sub = if (connected) null else "belum ada data",
                    disabled = !connected,
                    modifier = Modifier.weight(1f),
                )
            }

            Box(modifier = Modifier.padding(top = 18.dp, start = 20.dp, end = 20.dp)) {
                WindowToggle(disabled = !connected)
            }

            Box(modifier = Modifier.padding(top = 18.dp, start = 20.dp, end = 20.dp)) {
                BpmTrend(empty = !connected, onSeeAll = onSeeAllTrend)
            }
        }

        ThesisNav(
            actions = navActions,
            active = ThesisTab.Home,
            fabMode = ThesisFabMode.Pinwheel,
            fabDisabled = !connected,
            modifier = Modifier.padding(bottom = 16.dp),
        )
    }
}

@Composable
private fun SignalBars(level: Int, modifier: Modifier = Modifier) {
    val color = when {
        level >= 3 -> ThesisColors.Green
        level == 2 -> ThesisColors.Amber
        else -> ThesisColors.Red
    }
    val heights = listOf(6.dp, 10.dp, 14.dp)
    Row(modifier = modifier, verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
        heights.forEachIndexed { i, h ->
            Box(
                modifier = Modifier
                    .width(3.5.dp)
                    .height(h)
                    .clip(RoundedCornerShape(1.dp))
                    .background(if (i < level) color else OnixColors.BgTertiary),
            )
        }
    }
}

@Composable
private fun BpmGauge(
    value: Int,
    pct: Float,
    empty: Boolean,
    modifier: Modifier = Modifier,
    size: Dp = 230.dp,
) {
    val containerHeight = size * 0.82f
    Box(modifier = modifier.width(size).height(containerHeight)) {
        Canvas(
            modifier = Modifier
                .size(size)
                .align(Alignment.TopCenter)
                .offset(y = (-6).dp),
        ) {
            val strokeWidth = 16.dp.toPx()
            val r = 96.dp.toPx()
            val cx = this.size.width / 2
            val cy = this.size.height / 2
            val start = 135f
            val sweep = 270f
            drawArc(
                color = OnixColors.BgTertiary,
                startAngle = start - 90f,
                sweepAngle = sweep,
                useCenter = false,
                topLeft = Offset(cx - r, cy - r),
                size = Size(r * 2, r * 2),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
            )
            if (!empty) {
                val activeSweep = sweep * pct
                drawArc(
                    color = OnixColors.Red,
                    startAngle = start - 90f,
                    sweepAngle = activeSweep,
                    useCenter = false,
                    topLeft = Offset(cx - r, cy - r),
                    size = Size(r * 2, r * 2),
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                )
                val dotAngleRad = (start + activeSweep - 90f) * (PI / 180f).toFloat()
                val dx = cx + r * cos(dotAngleRad)
                val dy = cy + r * sin(dotAngleRad)
                drawCircle(color = Color.White, radius = 7.dp.toPx(), center = Offset(dx, dy))
                drawCircle(
                    color = OnixColors.Red,
                    radius = 7.dp.toPx(),
                    center = Offset(dx, dy),
                    style = Stroke(width = 2.dp.toPx()),
                )
            }
        }
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = containerHeight * 0.26f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = if (empty) "—" else "$value",
                color = if (empty) OnixColors.Fg3 else OnixColors.Fg1,
                fontFamily = EditorialFontFamily,
                fontWeight = FontWeight.W200,
                fontSize = 60.sp,
                lineHeight = 60.sp,
            )
            Text(
                text = if (empty) "BPM · Tidak Ada Data" else "BPM · sesi terakhir",
                color = if (empty) OnixColors.Fg3 else OnixColors.Fg2,
                fontFamily = SwitzerFontFamily,
                fontSize = 13.sp,
                modifier = Modifier.padding(top = 4.dp),
            )
            if (!empty) {
                Box(modifier = Modifier.padding(top = 10.dp)) {
                    StatusPill(text = "NORMAL", color = ThesisColors.Green, background = ThesisColors.GreenBg)
                }
            }
        }
    }
}

@Composable
private fun HtStatCard(
    icon: String,
    iconColor: Color,
    label: String,
    value: String,
    unit: String?,
    sub: String?,
    disabled: Boolean,
    modifier: Modifier = Modifier,
) {
    OnixCard(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(7.dp)) {
            OnixIcon(name = icon, size = 17.dp, color = if (disabled) OnixColors.Fg3 else iconColor, weight = 2f)
            Text(
                text = label,
                color = if (disabled) OnixColors.Fg2 else OnixColors.Fg1,
                fontFamily = SwitzerFontFamily,
                fontSize = 14.sp,
            )
        }
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.padding(top = 14.dp),
        ) {
            Text(
                text = value,
                color = if (disabled) OnixColors.Fg3 else OnixColors.Fg1,
                fontFamily = EditorialFontFamily,
                fontWeight = FontWeight.W300,
                fontSize = 40.sp,
                lineHeight = 36.sp,
            )
            if (unit != null) {
                Text(
                    text = " $unit",
                    color = OnixColors.Fg2,
                    fontFamily = EditorialFontFamily,
                    fontSize = 19.sp,
                )
            }
        }
        if (sub != null) {
            Text(
                text = sub,
                color = OnixColors.Fg3,
                fontFamily = SwitzerFontFamily,
                fontSize = 11.sp,
                modifier = Modifier.padding(top = 6.dp),
            )
        }
    }
}

@Composable
private fun ConditionCard(empty: Boolean, abnormal: Boolean = false, modifier: Modifier = Modifier) {
    OnixCard(modifier = modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OnixIcon(name = "heart", size = 18.dp, color = if (empty) OnixColors.Fg3 else OnixColors.Red, weight = 2f)
            Text(
                text = when {
                    empty -> "Belum ada sesi hari ini"
                    abnormal -> "1 dari 2 sesi abnormal"
                    else -> "2 sesi hari ini · Semua normal"
                },
                color = if (empty) OnixColors.Fg2 else OnixColors.Fg1,
                fontFamily = SwitzerFontFamily,
                fontSize = 14.sp,
                modifier = Modifier.weight(1f),
            )
            when {
                empty -> StatusPill(text = "—", color = OnixColors.Fg3, background = OnixColors.BgTertiary, bordered = false, leading = {})
                abnormal -> StatusPill(text = "⚠ Periksa", color = ThesisColors.Amber, background = ThesisColors.AmberBg, leading = {})
                else -> StatusPill(text = "✓ Normal", color = ThesisColors.Green, background = ThesisColors.GreenBg, leading = {})
            }
        }
    }
}

@Composable
private fun WindowToggle(disabled: Boolean, modifier: Modifier = Modifier) {
    var n by remember { mutableStateOf("10") }
    Column(modifier = modifier.fillMaxWidth().alpha(if (disabled) 0.5f else 1f)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row {
                Text(text = "Window Size ", color = OnixColors.Fg1, fontFamily = SwitzerFontFamily, fontWeight = FontWeight.W600, fontSize = 13.sp)
                Text(text = "(N)", color = OnixColors.Fg3, fontFamily = SwitzerFontFamily, fontSize = 13.sp)
            }
            StatusPill(text = "MA Filter", color = ThesisColors.Purple, background = ThesisColors.PurpleBg, leading = {})
        }
        Row(modifier = Modifier.fillMaxWidth().padding(top = 9.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("5", "10", "15", "20").forEach { v ->
                val selected = n == v
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp)
                        .clip(RoundedCornerShape(OnixRadius.full))
                        .background(if (selected) OnixColors.Red else OnixColors.BgSecondary)
                        .let { if (selected) it.insetTopGlow(OnixColors.RedGlow, height = 6.dp) else it }
                        .clickable(enabled = !disabled) { n = v },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = v,
                        color = if (selected) Color.White else OnixColors.Fg2,
                        fontFamily = SwitzerFontFamily,
                        fontWeight = FontWeight.W600,
                        fontSize = 14.sp,
                    )
                }
            }
        }
    }
}

private data class TrendDay(val day: String, val v: Int)

@Composable
private fun BpmTrend(empty: Boolean, onSeeAll: () -> Unit, modifier: Modifier = Modifier) {
    val data = if (empty) {
        listOf("Sen", "Sel", "Rab", "Kam", "Jum", "Sab", "Min").map { TrendDay(it, 0) }
    } else {
        listOf(
            TrendDay("Sen", 72), TrendDay("Sel", 78), TrendDay("Rab", 0), TrendDay("Kam", 81),
            TrendDay("Jum", 76), TrendDay("Sab", 74), TrendDay("Min", 79),
        )
    }
    val max = 90
    val chartHeight = 86.dp

    OnixCard(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
        ) {
            Text(text = "Tren BPM · 7 Hari", color = OnixColors.Fg1, fontFamily = SwitzerFontFamily, fontWeight = FontWeight.W600, fontSize = 13.sp)
            if (empty) {
                Text(text = "Belum ada data", color = OnixColors.Fg3, fontFamily = SwitzerFontFamily, fontSize = 12.sp)
            } else {
                Text(
                    text = "Lihat semua →",
                    color = OnixColors.Fg3,
                    fontFamily = SwitzerFontFamily,
                    fontSize = 12.sp,
                    modifier = Modifier.clickable(onClick = onSeeAll),
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth().height(chartHeight).padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.Bottom,
        ) {
            data.forEach { day ->
                val barEmpty = day.v == 0
                val barHeight = if (barEmpty) 6.dp else max(10f, (day.v / max.toFloat()) * chartHeight.value).dp
                Column(
                    modifier = Modifier.weight(1f).fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom,
                ) {
                    if (!barEmpty) {
                        Text(
                            text = "${day.v}",
                            color = OnixColors.Fg3,
                            fontFamily = SwitzerFontFamily,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(bottom = 5.dp),
                        )
                    }
                    Box(
                        modifier = Modifier
                            .width(22.dp)
                            .height(barHeight)
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (barEmpty) ThesisColors.Slate else ThesisColors.Red),
                    )
                }
            }
        }
        Row(modifier = Modifier.fillMaxWidth().padding(top = 9.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            data.forEach { day ->
                Text(
                    text = day.day,
                    color = OnixColors.Fg3,
                    fontFamily = SwitzerFontFamily,
                    fontSize = 10.sp,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}
