package com.onix.app.screens.home

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.onix.app.R
import com.onix.app.data.ppg.PpgWaveform
import com.onix.app.data.ppg.generatePpg
import com.onix.app.ui.components.Avatar
import com.onix.app.ui.components.OnixCard
import com.onix.app.ui.components.StatusPill
import com.onix.app.ui.components.ThesisFabMode
import com.onix.app.ui.components.ThesisNav
import com.onix.app.ui.components.ThesisNavActions
import com.onix.app.ui.icons.OnixIcon
import com.onix.app.ui.theme.EditorialFontFamily
import com.onix.app.ui.theme.OnixColors
import com.onix.app.ui.theme.SwitzerFontFamily
import com.onix.app.ui.theme.ThesisColors
import kotlinx.coroutines.delay

/**
 * Ported from screen_home_thesis.jsx's HomeLiveScreen / HomeLiveBody (24 · Live Monitoring).
 * [HomeLiveBody] is shared with the not-yet-built Session Ended screen, which reuses it frozen
 * (static sec/bpm, no PPG scroll, no LIVE dot pulse) as a backdrop behind its bottom sheet.
 */
@Composable
fun HomeLiveScreen(
    navActions: ThesisNavActions,
    modifier: Modifier = Modifier,
) {
    var sec by remember { mutableStateOf(222) }
    var bpm by remember { mutableStateOf(78) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            sec++
        }
    }
    LaunchedEffect(Unit) {
        while (true) {
            delay(1300)
            bpm = 76 + (0..5).random()
        }
    }

    Column(modifier = modifier.fillMaxSize().statusBarsPadding()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 110.dp),
        ) {
            HomeLiveBody(sec = sec, bpm = bpm, frozen = false)
        }
        ThesisNav(
            actions = navActions,
            fabMode = ThesisFabMode.Stop,
            modifier = Modifier.padding(bottom = 16.dp),
        )
    }
}

@Composable
fun HomeLiveBody(sec: Int, bpm: Int, frozen: Boolean, modifier: Modifier = Modifier) {
    val waveform = remember { generatePpg(7) }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 4.dp, start = 24.dp, end = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                LiveDot(frozen = frozen)
                Text(
                    text = "LIVE  00:${formatMmss(sec)}",
                    color = OnixColors.Red,
                    fontFamily = SwitzerFontFamily,
                    fontWeight = FontWeight.W600,
                    fontSize = 13.sp,
                )
            }
            Avatar(src = R.drawable.avatar_brian, size = 32.dp)
        }

        Box(modifier = Modifier.padding(top = 12.dp, start = 24.dp, end = 24.dp)) {
            StatusPill(text = "ESP32 Terhubung", color = ThesisColors.Green, background = ThesisColors.GreenBg)
        }

        Column(
            modifier = Modifier.fillMaxWidth().padding(top = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = "$bpm",
                    color = OnixColors.Fg1,
                    fontFamily = EditorialFontFamily,
                    fontWeight = FontWeight.W200,
                    fontSize = 74.sp,
                    lineHeight = 74.sp,
                )
                Text(text = " BPM", color = OnixColors.Fg2, fontFamily = SwitzerFontFamily, fontSize = 18.sp)
            }
            Box(modifier = Modifier.padding(top = 10.dp)) {
                StatusPill(text = "NORMAL", color = ThesisColors.Green, background = ThesisColors.GreenBg)
            }
        }

        Box(modifier = Modifier.padding(top = 20.dp, start = 20.dp, end = 20.dp)) {
            OnixCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(text = "Sinyal PPG", color = OnixColors.Fg3, fontFamily = SwitzerFontFamily, fontSize = 11.sp)
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        PpgLegendItem(label = "Raw", dashed = true, color = OnixColors.Fg3)
                        PpgLegendItem(label = "Filtered", dashed = false, color = OnixColors.Red)
                    }
                }
                PpgCanvas(
                    waveform = waveform,
                    frozen = frozen,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(waveform.tileHeight.dp)
                        .padding(top = 8.dp),
                )
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    StatusPill(text = "MA · N=10", color = ThesisColors.Purple, background = ThesisColors.PurpleBg, leading = {})
                    Text(text = "5 detik", color = OnixColors.Fg3, fontFamily = SwitzerFontFamily, fontSize = 9.sp)
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 14.dp, start = 20.dp, end = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            HtChip(label = "Min", value = "62", modifier = Modifier.weight(1f))
            HtChip(label = "Avg", value = "76", modifier = Modifier.weight(1f))
            HtChip(label = "Max", value = "89", modifier = Modifier.weight(1f))
        }

        Box(modifier = Modifier.padding(top = 12.dp, start = 20.dp, end = 20.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(OnixColors.BgSecondary)
                    .padding(vertical = 13.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OnixIcon(name = "clock", size = 16.dp, color = OnixColors.Fg2, weight = 2f)
                    Text(text = "Durasi: ${formatMmss(sec)}", color = OnixColors.Fg1, fontFamily = SwitzerFontFamily, fontSize = 14.sp)
                }
                Text(text = "Sesi aktif", color = OnixColors.Fg2, fontFamily = SwitzerFontFamily, fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun LiveDot(frozen: Boolean, modifier: Modifier = Modifier) {
    Box(modifier = modifier.size(8.dp), contentAlignment = Alignment.Center) {
        if (!frozen) {
            val transition = rememberInfiniteTransition(label = "liveDotPulse")
            val progress by transition.animateFloat(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(tween(durationMillis = 1600, easing = LinearOutSlowInEasing)),
                label = "liveDotPulseRing",
            )
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .graphicsLayer {
                        scaleX = 1f + progress * 2.25f
                        scaleY = 1f + progress * 2.25f
                        alpha = (1f - progress) * 0.5f
                    }
                    .clip(CircleShape)
                    .background(OnixColors.Red),
            )
        }
        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(OnixColors.Red))
    }
}

@Composable
private fun PpgLegendItem(label: String, dashed: Boolean, color: Color, modifier: Modifier = Modifier) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
        Canvas(modifier = Modifier.width(14.dp).height(2.dp)) {
            val y = size.height / 2
            drawLine(
                color = color,
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = size.height,
                pathEffect = if (dashed) PathEffect.dashPathEffect(floatArrayOf(4f, 3f)) else null,
            )
        }
        Text(text = label, color = OnixColors.Fg3, fontFamily = SwitzerFontFamily, fontSize = 9.sp)
    }
}

@Composable
private fun PpgCanvas(waveform: PpgWaveform, frozen: Boolean, modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "ppgScroll")
    val offsetPx by transition.animateFloat(
        initialValue = 0f,
        targetValue = waveform.tileWidth,
        animationSpec = infiniteRepeatable(tween(durationMillis = 5500, easing = LinearEasing)),
        label = "ppgScrollOffset",
    )
    val translateX = if (frozen) 0f else offsetPx

    Box(modifier = modifier.clipToBounds()) {
        Canvas(
            modifier = Modifier
                .width((waveform.tileWidth * 2).dp)
                .height(waveform.tileHeight.dp)
                .offset(x = -translateX.dp),
        ) {
            fun tiledPath(norm: FloatArray): Path {
                val path = Path()
                for (tile in 0..1) {
                    for (i in 0 until waveform.sampleCount) {
                        val x = tile * waveform.tileWidth + (i / (waveform.sampleCount - 1).toFloat()) * waveform.tileWidth
                        val y = waveform.tileHeight - (0.12f + norm[i] * 0.74f) * waveform.tileHeight
                        if (tile == 0 && i == 0) path.moveTo(x, y) else path.lineTo(x, y)
                    }
                }
                return path
            }
            drawPath(
                path = tiledPath(waveform.rawNorm),
                color = OnixColors.Fg3.copy(alpha = 0.6f),
                style = Stroke(width = 1.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round),
            )
            drawPath(
                path = tiledPath(waveform.filteredNorm),
                color = OnixColors.Red,
                style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round),
            )
        }
    }
}

@Composable
private fun HtChip(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(OnixColors.BgSecondary)
            .padding(vertical = 10.dp, horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = label, color = OnixColors.Fg3, fontFamily = SwitzerFontFamily, fontSize = 10.sp)
        Row(modifier = Modifier.padding(top = 3.dp), verticalAlignment = Alignment.Bottom) {
            Text(text = value, color = OnixColors.Fg1, fontFamily = EditorialFontFamily, fontWeight = FontWeight.W300, fontSize = 22.sp)
            Text(text = " BPM", color = OnixColors.Fg3, fontFamily = SwitzerFontFamily, fontSize = 10.sp)
        }
    }
}

private fun formatMmss(totalSeconds: Int): String {
    val m = totalSeconds / 60
    val s = totalSeconds % 60
    return "%02d:%02d".format(m, s)
}
