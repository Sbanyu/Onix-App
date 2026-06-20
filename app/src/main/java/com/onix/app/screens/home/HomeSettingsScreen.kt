package com.onix.app.screens.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.onix.app.ui.components.OnixButton
import com.onix.app.ui.components.OnixCard
import com.onix.app.ui.components.SettingsGroup
import com.onix.app.ui.components.SettingsRow
import com.onix.app.ui.components.SettingsToggle
import com.onix.app.ui.components.SettingsValue
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
import kotlin.math.roundToInt
import kotlin.math.sin

private enum class MonSheetKind { Filter, Bpm, Rate }

/**
 * Ported from screen_settings.jsx's HomeSettingsScreen (27 · Pengaturan): a connected-device
 * card over four SettingsGroups, with three monitoring values (Filter Sinyal, Ambang BPM,
 * Sampling Rate) edited through draft state in a shared [MonSheet] bottom sheet, committed
 * back to the real state only on "Simpan".
 */
@Composable
fun HomeSettingsScreen(
    navActions: ThesisNavActions,
    onManageDevice: () -> Unit,
    onOpenPrivacy: () -> Unit,
    onOpenAbout: () -> Unit,
    onSignOut: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var notif by remember { mutableStateOf(true) }
    var alert by remember { mutableStateOf(true) }
    var dark by remember { mutableStateOf(true) }
    var autorec by remember { mutableStateOf(false) }

    var sheet by remember { mutableStateOf<MonSheetKind?>(null) }
    var winN by remember { mutableStateOf(10) }
    var bpmMin by remember { mutableStateOf(60) }
    var bpmMax by remember { mutableStateOf(100) }
    var rate by remember { mutableStateOf(100) }
    var draftN by remember { mutableStateOf(10) }
    var draftMin by remember { mutableStateOf(60) }
    var draftMax by remember { mutableStateOf(100) }
    var draftRate by remember { mutableStateOf(100) }

    fun openSheet(kind: MonSheetKind) {
        when (kind) {
            MonSheetKind.Filter -> draftN = winN
            MonSheetKind.Bpm -> { draftMin = bpmMin; draftMax = bpmMax }
            MonSheetKind.Rate -> draftRate = rate
        }
        sheet = kind
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 110.dp),
            ) {
                Text(
                    text = "Pengaturan",
                    color = OnixColors.Fg1,
                    fontFamily = EditorialFontFamily,
                    fontWeight = FontWeight.W300,
                    fontSize = 40.sp,
                    modifier = Modifier.padding(top = 8.dp, start = 24.dp, end = 24.dp),
                )

                Box(modifier = Modifier.fillMaxWidth().padding(top = 18.dp, start = 20.dp, end = 20.dp)) {
                    OnixCard(modifier = Modifier.fillMaxWidth()) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                            Box(
                                modifier = Modifier.size(46.dp).clip(RoundedCornerShape(13.dp)).background(ThesisColors.GreenBg),
                                contentAlignment = Alignment.Center,
                            ) {
                                OnixIcon(name = "bluetooth", size = 22.dp, color = ThesisColors.Green, weight = 2f)
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = "ESP32_HeartMonitor", color = OnixColors.Fg1, fontFamily = SwitzerFontFamily, fontWeight = FontWeight.W600, fontSize = 15.5.sp)
                                Row(
                                    modifier = Modifier.padding(top = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                                ) {
                                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(ThesisColors.Green))
                                    Text(text = "Terhubung · Bluetooth Classic", color = ThesisColors.Green, fontFamily = SwitzerFontFamily, fontSize = 12.sp)
                                }
                            }
                            Box(
                                modifier = Modifier
                                    .height(32.dp)
                                    .clip(RoundedCornerShape(OnixRadius.full))
                                    .border(1.dp, OnixColors.Border2, RoundedCornerShape(OnixRadius.full))
                                    .clickable(onClick = onManageDevice)
                                    .padding(horizontal = 14.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(text = "Kelola", color = OnixColors.Fg2, fontFamily = SwitzerFontFamily, fontWeight = FontWeight.W600, fontSize = 12.sp)
                            }
                        }
                    }
                }

                SettingsGroup(
                    title = "Monitoring",
                    rows = listOf(
                        {
                            SettingsRow(
                                icon = "chartBar",
                                label = "Filter Sinyal",
                                sub = "Moving Average · N = $winN",
                                onClick = { openSheet(MonSheetKind.Filter) },
                                trailing = { SettingsValue("N=$winN") },
                            )
                        },
                        {
                            SettingsRow(
                                icon = "heart",
                                label = "Ambang BPM",
                                sub = "Batas normal: $bpmMin–$bpmMax BPM",
                                onClick = { openSheet(MonSheetKind.Bpm) },
                                trailing = { SettingsValue("$bpmMin–$bpmMax") },
                            )
                        },
                        {
                            SettingsRow(
                                icon = "scan",
                                label = "Sampling Rate",
                                onClick = { openSheet(MonSheetKind.Rate) },
                                trailing = { SettingsValue("$rate Hz") },
                            )
                        },
                        {
                            SettingsRow(
                                icon = "play",
                                label = "Rekam otomatis",
                                sub = "Mulai sesi saat sensor terdeteksi",
                                trailing = { SettingsToggle(on = autorec, onToggle = { autorec = !autorec }) },
                            )
                        },
                    ),
                )

                SettingsGroup(
                    title = "Notifikasi",
                    rows = listOf(
                        { SettingsRow(icon = "bell", label = "Notifikasi", trailing = { SettingsToggle(on = notif, onToggle = { notif = !notif }) }) },
                        {
                            SettingsRow(
                                icon = "warning",
                                iconColor = OnixColors.Red,
                                label = "Peringatan abnormal",
                                sub = "Beri tahu jika BPM di luar batas",
                                trailing = { SettingsToggle(on = alert, onToggle = { alert = !alert }) },
                            )
                        },
                    ),
                )

                SettingsGroup(
                    title = "Preferensi",
                    rows = listOf(
                        { SettingsRow(icon = "house", label = "Mode Gelap", trailing = { SettingsToggle(on = dark, onToggle = { dark = !dark }) }) },
                        { SettingsRow(icon = "globe", label = "Bahasa", trailing = { SettingsValue("Indonesia") }) },
                    ),
                )

                SettingsGroup(
                    title = "Lainnya",
                    rows = listOf(
                        {
                            SettingsRow(
                                icon = "shield",
                                label = "Privasi & Keamanan",
                                onClick = onOpenPrivacy,
                                trailing = { OnixIcon(name = "caretRight", size = 16.dp, color = OnixColors.Fg3, weight = 2f) },
                            )
                        },
                        {
                            SettingsRow(
                                icon = "info",
                                label = "Tentang Onix",
                                sub = "Versi 1.0.0",
                                onClick = onOpenAbout,
                                trailing = { OnixIcon(name = "caretRight", size = 16.dp, color = OnixColors.Fg3, weight = 2f) },
                            )
                        },
                        { SettingsRow(icon = "signOut", label = "Keluar", danger = true, onClick = onSignOut) },
                    ),
                )
            }
            ThesisNav(actions = navActions, active = ThesisTab.Pengaturan, modifier = Modifier.padding(bottom = 16.dp))
        }

        when (sheet) {
            MonSheetKind.Filter -> MonSheet(
                title = "Filter Sinyal",
                sub = "Moving Average menghaluskan sinyal PPG. Pilih ukuran window (N).",
                onClose = { sheet = null },
                onSave = { winN = draftN; sheet = null },
            ) {
                WindowPicker(value = draftN, onChange = { draftN = it })
            }
            MonSheetKind.Bpm -> MonSheet(
                title = "Ambang BPM",
                sub = "Atur rentang detak jantung yang dianggap normal.",
                onClose = { sheet = null },
                onSave = { bpmMin = draftMin; bpmMax = draftMax; sheet = null },
            ) {
                BpmRange(min = draftMin, max = draftMax, onMinChange = { draftMin = it }, onMaxChange = { draftMax = it })
            }
            MonSheetKind.Rate -> MonSheet(
                title = "Sampling Rate",
                sub = "Frekuensi pembacaan sensor MAX30102 per detik.",
                onClose = { sheet = null },
                onSave = { rate = draftRate; sheet = null },
            ) {
                RatePicker(value = draftRate, onChange = { draftRate = it })
            }
            null -> Unit
        }
    }
}

@Composable
private fun MonSheet(
    title: String,
    sub: String,
    onClose: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.62f))
                .clickable(onClick = onClose),
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp))
                .background(OnixColors.BgSecondary)
                .padding(start = 22.dp, end = 22.dp, top = 12.dp, bottom = 26.dp),
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 18.dp)
                    .width(40.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(OnixRadius.full))
                    .background(OnixColors.Border2),
            )
            Text(text = title, color = OnixColors.Fg1, fontFamily = EditorialFontFamily, fontWeight = FontWeight.W300, fontSize = 27.sp, lineHeight = 28.35.sp)
            Text(text = sub, color = OnixColors.Fg2, fontFamily = SwitzerFontFamily, fontSize = 13.sp, lineHeight = 19.5.sp, modifier = Modifier.padding(top = 8.dp))
            Box(modifier = Modifier.padding(top = 22.dp)) { content() }
            OnixButton(text = "Simpan", onClick = onSave, fullWidth = true, modifier = Modifier.padding(top = 24.dp))
        }
    }
}

@Composable
private fun WindowPicker(value: Int, onChange: (Int) -> Unit, modifier: Modifier = Modifier) {
    val pts = 60
    val raw = remember { FloatArray(pts) { i -> windowPickerRnd(i) } }
    val filt = remember(value) {
        val n = maxOf(1, (value / 2.0).roundToInt())
        FloatArray(pts) { i ->
            var s = 0f
            var c = 0
            for (k in -n + 1..0) {
                val j = i + k
                if (j >= 0) { s += raw[j]; c++ }
            }
            s / c
        }
    }
    val mn = remember(filt) { (raw + filt).min() }
    val mx = remember(filt) { (raw + filt).max() }
    val rg = if (mx - mn == 0f) 1f else mx - mn

    Column(modifier = modifier) {
        Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(OnixColors.BgTertiary).padding(14.dp)) {
            Column {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                        Canvas(modifier = Modifier.width(14.dp).height(2.dp)) {
                            drawLine(
                                color = OnixColors.Fg3,
                                start = Offset(0f, size.height / 2),
                                end = Offset(size.width, size.height / 2),
                                strokeWidth = size.height,
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(3f, 3f)),
                            )
                        }
                        Text(text = "Raw", color = OnixColors.Fg3, fontFamily = SwitzerFontFamily, fontSize = 9.sp)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                        Box(modifier = Modifier.width(14.dp).height(2.dp).background(OnixColors.Red))
                        Text(text = "Filtered", color = OnixColors.Fg3, fontFamily = SwitzerFontFamily, fontSize = 9.sp)
                    }
                }
                Canvas(modifier = Modifier.fillMaxWidth().height(64.dp).padding(top = 8.dp)) {
                    fun pointX(i: Int) = i / (pts - 1).toFloat() * size.width
                    fun pointY(v: Float) = size.height - 8.dp.toPx() - ((v - mn) / rg) * (size.height - 16.dp.toPx())

                    val rawPath = Path().apply {
                        moveTo(pointX(0), pointY(raw[0]))
                        for (i in 1 until pts) lineTo(pointX(i), pointY(raw[i]))
                    }
                    val filtPath = Path().apply {
                        moveTo(pointX(0), pointY(filt[0]))
                        for (i in 1 until pts) lineTo(pointX(i), pointY(filt[i]))
                    }
                    drawPath(rawPath, color = OnixColors.Fg3, alpha = 0.55f, style = Stroke(width = 1.dp.toPx()))
                    drawPath(filtPath, color = OnixColors.Red, style = Stroke(width = 2.2.dp.toPx(), join = StrokeJoin.Round))
                }
            }
        }
        Row(modifier = Modifier.fillMaxWidth().padding(top = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(5, 10, 15, 20).forEach { n ->
                val selected = value == n
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (selected) OnixColors.Red else OnixColors.BgTertiary)
                        .let { if (selected) it.insetTopGlow(OnixColors.RedGlow, height = 3.dp) else it }
                        .clickable(onClick = { onChange(n) }),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = "$n", color = if (selected) OnixColors.White else OnixColors.Fg2, fontFamily = EditorialFontFamily, fontWeight = FontWeight.W300, fontSize = 24.sp)
                }
            }
        }
        Text(
            text = "N lebih besar = sinyal lebih halus, respons lebih lambat",
            color = OnixColors.Fg3,
            fontFamily = SwitzerFontFamily,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
        )
    }
}

private fun windowPickerRnd(i: Int): Float {
    val noise = (i * 9301 + 49297) % 233 / 233f - 0.5f
    return (sin(i * 0.9) + 0.5 * sin(i * 2.3 + 1) + noise * 1.4).toFloat()
}

@Composable
private fun BpmRange(min: Int, max: Int, onMinChange: (Int) -> Unit, onMaxChange: (Int) -> Unit, modifier: Modifier = Modifier) {
    val lo = 40
    val hi = 180
    val density = LocalDensity.current

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(26.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.Bottom,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "MIN", color = OnixColors.Fg3, fontFamily = SwitzerFontFamily, fontSize = 11.sp, letterSpacing = 1.1.sp)
                Text(text = "$min", color = OnixColors.Fg1, fontFamily = EditorialFontFamily, fontWeight = FontWeight.W300, fontSize = 44.sp, lineHeight = 44.sp, modifier = Modifier.padding(top = 4.dp))
            }
            Text(text = "–", color = OnixColors.Fg3, fontFamily = SwitzerFontFamily, fontSize = 22.sp, modifier = Modifier.padding(bottom = 6.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "MAX", color = OnixColors.Fg3, fontFamily = SwitzerFontFamily, fontSize = 11.sp, letterSpacing = 1.1.sp)
                Row(modifier = Modifier.padding(top = 4.dp), verticalAlignment = Alignment.Bottom) {
                    Text(text = "$max", color = OnixColors.Fg1, fontFamily = EditorialFontFamily, fontWeight = FontWeight.W300, fontSize = 44.sp, lineHeight = 44.sp)
                    Text(text = " BPM", color = OnixColors.Fg3, fontFamily = SwitzerFontFamily, fontSize = 14.sp)
                }
            }
        }

        var trackWidthPx by remember { mutableStateOf(0f) }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 22.dp)
                .height(40.dp)
                .onSizeChanged { trackWidthPx = it.width.toFloat() },
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 17.dp)
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(OnixRadius.full))
                    .background(OnixColors.BgTertiary),
            )
            val minPct = ((min - lo).toFloat() / (hi - lo)).coerceIn(0f, 1f)
            val maxPct = ((max - lo).toFloat() / (hi - lo)).coerceIn(0f, 1f)
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 17.dp)
                    .offset { IntOffset((minPct * trackWidthPx).roundToInt(), 0) }
                    .width(with(density) { ((maxPct - minPct) * trackWidthPx).toDp() })
                    .height(6.dp)
                    .clip(RoundedCornerShape(OnixRadius.full))
                    .background(OnixColors.Red),
            )
            BpmThumb(
                value = min,
                lo = lo,
                hi = hi,
                trackWidthPx = trackWidthPx,
                onValueChange = { v -> onMinChange(v.coerceAtMost(max - 5)) },
                modifier = Modifier.align(Alignment.TopStart),
            )
            BpmThumb(
                value = max,
                lo = lo,
                hi = hi,
                trackWidthPx = trackWidthPx,
                onValueChange = { v -> onMaxChange(v.coerceAtLeast(min + 5)) },
                modifier = Modifier.align(Alignment.TopStart),
            )
        }
        Row(modifier = Modifier.fillMaxWidth().padding(top = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "$lo", color = OnixColors.Fg3, fontFamily = SwitzerFontFamily, fontSize = 11.sp)
            Text(text = "$hi BPM", color = OnixColors.Fg3, fontFamily = SwitzerFontFamily, fontSize = 11.sp)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 18.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(OnixColors.Red.copy(alpha = 0.08f))
                .border(1.dp, OnixColors.Red.copy(alpha = 0.25f), RoundedCornerShape(12.dp))
                .padding(vertical = 12.dp, horizontal = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OnixIcon(name = "warning", size = 16.dp, color = OnixColors.Red, weight = 2f)
            Text(
                text = buildAnnotatedString {
                    append("BPM di luar rentang ini ditandai ")
                    withStyle(SpanStyle(color = OnixColors.Red, fontWeight = FontWeight.Bold)) { append("abnormal") }
                    append(".")
                },
                color = OnixColors.Fg2,
                fontFamily = SwitzerFontFamily,
                fontSize = 12.sp,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun BpmThumb(
    value: Int,
    lo: Int,
    hi: Int,
    trackWidthPx: Float,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    val currentValue by rememberUpdatedState(value)
    val pct = ((value - lo).toFloat() / (hi - lo)).coerceIn(0f, 1f)
    val thumbRadiusPx = with(density) { 12.dp.toPx() }

    Box(
        modifier = modifier
            .offset {
                IntOffset(
                    x = (pct * trackWidthPx - thumbRadiusPx).roundToInt(),
                    y = with(density) { 8.dp.roundToPx() },
                )
            }
            .size(24.dp)
            .clip(CircleShape)
            .background(OnixColors.White)
            .pointerInput(trackWidthPx) {
                var startValue = currentValue
                var accumPx = 0f
                detectHorizontalDragGestures(
                    onDragStart = { startValue = currentValue; accumPx = 0f },
                    onHorizontalDrag = { change, dragAmount ->
                        change.consume()
                        accumPx += dragAmount
                        if (trackWidthPx > 0f) {
                            val newValue = (startValue + accumPx / trackWidthPx * (hi - lo)).roundToInt().coerceIn(lo, hi)
                            onValueChange(newValue)
                        }
                    },
                )
            },
    )
}

private data class RateOption(val hz: Int, val note: String)

private val rateOptions = listOf(
    RateOption(50, "Hemat daya"),
    RateOption(100, "Direkomendasikan"),
    RateOption(200, "Presisi tinggi"),
)

@Composable
private fun RatePicker(value: Int, onChange: (Int) -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        rateOptions.forEach { option ->
            val selected = value == option.hz
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (selected) OnixColors.Red.copy(alpha = 0.10f) else OnixColors.BgTertiary)
                    .border(1.dp, if (selected) OnixColors.Red else OnixColors.Border1, RoundedCornerShape(14.dp))
                    .clickable(onClick = { onChange(option.hz) })
                    .padding(vertical = 15.dp, horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(text = "${option.hz} ", color = OnixColors.Fg1, fontFamily = EditorialFontFamily, fontWeight = FontWeight.W300, fontSize = 22.sp)
                        Text(text = "Hz", color = OnixColors.Fg3, fontFamily = SwitzerFontFamily, fontSize = 13.sp)
                    }
                    Text(
                        text = option.note,
                        color = if (option.hz == 100) ThesisColors.Green else OnixColors.Fg3,
                        fontFamily = SwitzerFontFamily,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 2.dp),
                    )
                }
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .clip(CircleShape)
                        .border(2.dp, if (selected) OnixColors.Red else OnixColors.Border2, CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    if (selected) {
                        Box(modifier = Modifier.size(11.dp).clip(CircleShape).background(OnixColors.Red))
                    }
                }
            }
        }
    }
}
