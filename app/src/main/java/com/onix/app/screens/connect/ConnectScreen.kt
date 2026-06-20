package com.onix.app.screens.connect

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.onix.app.ui.components.OnixButton
import com.onix.app.ui.components.OnixButtonVariant
import com.onix.app.ui.icons.OnixIcon
import com.onix.app.ui.theme.EditorialFontFamily
import com.onix.app.ui.theme.OnixColors
import com.onix.app.ui.theme.SwitzerFontFamily
import com.onix.app.ui.theme.insetTopGlow
import kotlinx.coroutines.delay

private const val DEVICE = "ESP32_HeartMonitor"

private enum class ConnectStep { Initial, Scanning, Found, Pairing, Success, Failed }

/**
 * Ported from screen_connect.jsx: 6-state mocked BLE pairing flow (Bluetooth Classic SPP,
 * Just Works, no real radio access). Auto-advance timers mirror the original's
 * setTimeout(2600ms)/setTimeout(2800ms) via LaunchedEffect+delay.
 */
@Composable
fun ConnectScreen(
    onStartMonitoring: () -> Unit,
    onSkipToNoDevice: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var step by remember { mutableStateOf(ConnectStep.Initial) }

    LaunchedEffect(step) {
        when (step) {
            ConnectStep.Scanning -> {
                delay(2600)
                step = ConnectStep.Found
            }
            ConnectStep.Pairing -> {
                delay(2800)
                step = ConnectStep.Success
            }
            else -> {}
        }
    }

    when (step) {
        ConnectStep.Initial -> ConnectInitial(
            onScan = { step = ConnectStep.Scanning },
            onSkip = onSkipToNoDevice,
            modifier = modifier,
        )
        ConnectStep.Scanning -> ConnectScanning(
            onClose = { step = ConnectStep.Initial },
            onCancel = { step = ConnectStep.Initial },
            modifier = modifier,
        )
        ConnectStep.Found -> ConnectFound(
            onClose = { step = ConnectStep.Scanning },
            onRescan = { step = ConnectStep.Scanning },
            onPair = { step = ConnectStep.Pairing },
            modifier = modifier,
        )
        ConnectStep.Pairing -> ConnectPairing(
            onCancel = { step = ConnectStep.Found },
            modifier = modifier,
        )
        ConnectStep.Success -> ConnectSuccess(
            onStartMonitoring = onStartMonitoring,
            modifier = modifier,
        )
        ConnectStep.Failed -> ConnectFailed(
            onTryAgain = { step = ConnectStep.Pairing },
            onSkip = onSkipToNoDevice,
            modifier = modifier,
        )
    }
}

@Composable
private fun ConnectShell(
    onClose: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    body: @Composable ColumnScope.() -> Unit,
    actions: @Composable ColumnScope.() -> Unit,
) {
    Column(modifier = modifier.fillMaxSize().statusBarsPadding()) {
        Box(
            modifier = Modifier.fillMaxWidth().height(44.dp).padding(horizontal = 20.dp),
            contentAlignment = Alignment.CenterStart,
        ) {
            if (onClose != null) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(OnixColors.BgSecondary)
                        .clickable(onClick = onClose),
                    contentAlignment = Alignment.Center,
                ) {
                    OnixIcon(name = "caretLeft", size = 18.dp, color = OnixColors.Fg1)
                }
            }
        }
        Column(modifier = Modifier.weight(1f).fillMaxWidth().padding(horizontal = 28.dp)) {
            body()
        }
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 28.dp).padding(bottom = 30.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            actions()
        }
    }
}

@Composable
private fun ConnectHeading(text: String, sub: String? = null) {
    Column {
        Text(
            text = text,
            color = OnixColors.Fg1,
            fontFamily = EditorialFontFamily,
            fontWeight = FontWeight.W300,
            fontSize = 40.sp,
            lineHeight = 42.sp,
            letterSpacing = (-0.4).sp,
        )
        if (sub != null) {
            Text(
                text = sub,
                color = OnixColors.Fg2,
                fontFamily = SwitzerFontFamily,
                fontSize = 15.sp,
                lineHeight = 22.sp,
                modifier = Modifier.padding(top = 12.dp).widthIn(max = 300.dp),
            )
        }
    }
}

@Composable
private fun ColumnScope.ConnectCenter(gap: Dp = 26.dp, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier.weight(1f).fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(gap, Alignment.CenterVertically),
        content = content,
    )
}

@Composable
private fun ConnectTextButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Text(
        text = text,
        color = OnixColors.Fg3,
        fontFamily = SwitzerFontFamily,
        fontSize = 14.sp,
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(6.dp),
    )
}

/* ---- geometric pulse / heartbeat graphic (accent red) ---- */
@Composable
private fun PulseGraphic() {
    val heartPath = remember {
        PathParser().parsePathString(
            "M60 96 C60 96 22 70 22 44 a20 20 0 0 1 38 -10 a20 20 0 0 1 38 10 c0 26 -38 52 -38 52 Z",
        ).toPath()
    }
    val ecgPath = remember {
        PathParser().parsePathString("M26 60 H46 L52 44 L60 78 L68 54 L73 60 H94").toPath()
    }
    val dashEffect = remember { PathEffect.dashPathEffect(floatArrayOf(8f, 6f)) }

    Box(
        modifier = Modifier
            .size(188.dp)
            .clip(RoundedCornerShape(44.dp))
            .drawWithCache {
                val brush = Brush.radialGradient(
                    0f to Color(0x29EF4444),
                    0.6f to Color(0x0AEF4444),
                    1f to Color.Transparent,
                    center = Offset(size.width / 2f, 0f),
                    radius = size.width * 1.2f,
                )
                onDrawBehind { drawRect(brush) }
            }
            .border(1.dp, Color(0x38EF4444), RoundedCornerShape(44.dp)),
        contentAlignment = Alignment.Center,
    ) {
        Box(modifier = Modifier.size(136.dp).drawBehind {
            drawRoundRect(
                color = Color(0x47EF4444),
                style = Stroke(width = 1.dp.toPx(), pathEffect = dashEffect),
                cornerRadius = CornerRadius(32.dp.toPx(), 32.dp.toPx()),
            )
        })
        Box(modifier = Modifier.size(120.dp).drawBehind {
            val scale = size.width / 120f
            scale(scale, scale, pivot = Offset.Zero) {
                drawPath(heartPath, color = Color(0x1FEF4444))
                drawPath(
                    heartPath,
                    color = OnixColors.Red,
                    style = Stroke(width = 2.5f, cap = StrokeCap.Round, join = StrokeJoin.Round),
                )
                drawPath(
                    ecgPath,
                    color = OnixColors.Red,
                    style = Stroke(width = 3.5f, cap = StrokeCap.Round, join = StrokeJoin.Round),
                )
            }
        })
    }
}

/* ---- radar ping ---- */
@Composable
private fun Radar() {
    val transition = rememberInfiniteTransition(label = "radar")
    Box(modifier = Modifier.size(220.dp), contentAlignment = Alignment.Center) {
        repeat(3) { i ->
            val progress by transition.animateFloat(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 2400, easing = LinearEasing),
                    initialStartOffset = StartOffset(i * 800),
                ),
                label = "radarRing$i",
            )
            Box(
                modifier = Modifier
                    .size(220.dp)
                    .graphicsLayer {
                        scaleX = 0.4f + progress * 0.6f
                        scaleY = 0.4f + progress * 0.6f
                        alpha = 1f - progress
                    }
                    .clip(CircleShape)
                    .border(1.5.dp, OnixColors.Red, CircleShape),
            )
        }
        Box(
            modifier = Modifier
                .size(76.dp)
                .clip(CircleShape)
                .background(OnixColors.Red)
                .insetTopGlow(OnixColors.RedGlow, height = 5.dp),
            contentAlignment = Alignment.Center,
        ) {
            OnixIcon(name = "scan", size = 30.dp, color = Color.White, weight = 2f)
        }
    }
}

/* ---- pairing spinner (arc ring + pulsing device chip) ---- */
@Composable
private fun PairingSpinner() {
    val transition = rememberInfiniteTransition(label = "pairing")
    val rotation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(durationMillis = 1100, easing = LinearEasing)),
        label = "spin",
    )
    val pulse by transition.animateFloat(
        initialValue = 1f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            tween(durationMillis = 700),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "pulse",
    )
    Box(modifier = Modifier.size(200.dp), contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .size(200.dp)
                .padding(2.5.dp)
                .graphicsLayer { rotationZ = rotation }
                .drawBehind {
                    val strokeWidthPx = 5.dp.toPx()
                    drawArc(
                        color = OnixColors.BgTertiary,
                        startAngle = 0f,
                        sweepAngle = 360f,
                        useCenter = false,
                        style = Stroke(width = strokeWidthPx),
                    )
                    drawArc(
                        color = OnixColors.Red,
                        startAngle = -90f,
                        sweepAngle = 90f,
                        useCenter = false,
                        style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round),
                    )
                },
        )
        Box(
            modifier = Modifier
                .size(96.dp)
                .graphicsLayer { scaleX = pulse; scaleY = pulse }
                .clip(RoundedCornerShape(28.dp))
                .background(OnixColors.BgSecondary),
            contentAlignment = Alignment.Center,
        ) {
            OnixIcon(name = "heart", size = 40.dp, color = OnixColors.Red)
        }
    }
}

/* ---- result circle (success / fail) ---- */
@Composable
private fun ResultCircle(ok: Boolean) {
    val accent = if (ok) OnixColors.Success else OnixColors.Red
    val tint = if (ok) Color(0x2416B364) else Color(0x24EF4444)
    val glowColor = if (ok) Color(0x6616B364) else Color(0x66EF4444)
    val scale = remember { Animatable(0.6f) }
    LaunchedEffect(ok) {
        scale.snapTo(0.6f)
        scale.animateTo(
            1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow,
            ),
        )
    }
    Box(
        modifier = Modifier.size(200.dp).drawBehind {
            drawCircle(brush = Brush.radialGradient(listOf(glowColor, Color.Transparent), radius = size.minDimension / 2f))
        },
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(132.dp)
                .graphicsLayer { scaleX = scale.value; scaleY = scale.value }
                .clip(CircleShape)
                .background(tint),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(88.dp)
                    .clip(CircleShape)
                    .background(accent)
                    .insetTopGlow(if (ok) Color(0xB37CE7B2) else OnixColors.RedGlow, height = 5.dp),
                contentAlignment = Alignment.Center,
            ) {
                OnixIcon(name = if (ok) "check" else "x", size = 40.dp, color = Color.White, weight = 2.4f)
            }
        }
    }
}

/* ---- device row (found state) ---- */
@Composable
private fun DeviceRow(
    name: String,
    sub: String? = null,
    onPair: (() -> Unit)? = null,
    muted: Boolean = false,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .let { if (!muted) it.background(OnixColors.BgSecondary) else it }
            .border(1.dp, if (muted) OnixColors.Border1 else Color(0x59EF4444), RoundedCornerShape(14.dp))
            .alpha(if (muted) 0.55f else 1f)
            .padding(vertical = if (muted) 12.dp else 14.dp, horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(13.dp),
    ) {
        Box(
            modifier = Modifier
                .size(if (muted) 34.dp else 42.dp)
                .clip(RoundedCornerShape(11.dp))
                .background(if (muted) OnixColors.BgTertiary else Color(0x24EF4444)),
            contentAlignment = Alignment.Center,
        ) {
            OnixIcon(
                name = "heart",
                size = if (muted) 16.dp else 20.dp,
                color = if (muted) OnixColors.Fg3 else OnixColors.Red,
                weight = 2f,
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                color = OnixColors.Fg1,
                fontFamily = SwitzerFontFamily,
                fontWeight = FontWeight.W600,
                fontSize = if (muted) 14.sp else 15.5.sp,
                maxLines = 1,
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
        if (onPair != null) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .background(OnixColors.Red)
                    .insetTopGlow(OnixColors.RedGlow, height = 3.dp)
                    .clickable(onClick = onPair)
                    .padding(vertical = 9.dp, horizontal = 20.dp),
            ) {
                Text(text = "Pair", color = Color.White, fontFamily = SwitzerFontFamily, fontWeight = FontWeight.W600, fontSize = 14.sp)
            }
        }
    }
}

@Composable
private fun ConnectInitial(onScan: () -> Unit, onSkip: () -> Unit, modifier: Modifier = Modifier) {
    ConnectShell(
        modifier = modifier,
        body = {
            Box(modifier = Modifier.padding(top = 8.dp)) {
                ConnectHeading(text = "Connect Your\nDevice", sub = "Find and pair your heart rate sensor to start monitoring.")
            }
            ConnectCenter {
                PulseGraphic()
            }
        },
        actions = {
            OnixButton(text = "Scan for Devices", onClick = onScan, icon = "scan", fullWidth = true)
            ConnectTextButton(text = "Skip for now", onClick = onSkip)
        },
    )
}

@Composable
private fun ConnectScanning(onClose: () -> Unit, onCancel: () -> Unit, modifier: Modifier = Modifier) {
    ConnectShell(
        modifier = modifier,
        onClose = onClose,
        body = {
            Box(modifier = Modifier.padding(top = 8.dp)) { ConnectHeading(text = "Scanning…") }
            ConnectCenter(gap = 30.dp) {
                Radar()
                Text(text = "Looking for nearby devices", color = OnixColors.Fg2, fontFamily = SwitzerFontFamily, fontSize = 15.sp)
            }
            Column(
                modifier = Modifier.padding(bottom = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                listOf(0.5f, 0.32f).forEach { rowAlpha ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(OnixColors.BgSecondary)
                            .alpha(rowAlpha)
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(13.dp),
                    ) {
                        Box(modifier = Modifier.size(38.dp).clip(RoundedCornerShape(11.dp)).background(OnixColors.BgTertiary))
                        Column(modifier = Modifier.weight(1f)) {
                            Box(modifier = Modifier.fillMaxWidth(0.55f).height(11.dp).clip(RoundedCornerShape(6.dp)).background(OnixColors.BgTertiary))
                            Box(
                                modifier = Modifier
                                    .padding(top = 7.dp)
                                    .fillMaxWidth(0.35f)
                                    .height(9.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(OnixColors.BgTertiary),
                            )
                        }
                    }
                }
            }
        },
        actions = {
            OnixButton(text = "Cancel", onClick = onCancel, variant = OnixButtonVariant.Outline, fullWidth = true)
        },
    )
}

@Composable
private fun ConnectFound(onClose: () -> Unit, onRescan: () -> Unit, onPair: () -> Unit, modifier: Modifier = Modifier) {
    ConnectShell(
        modifier = modifier,
        onClose = onClose,
        body = {
            Box(modifier = Modifier.padding(top = 8.dp)) {
                ConnectHeading(text = "Select Your\nDevice", sub = "We found a compatible heart rate sensor nearby.")
            }
            Column(
                modifier = Modifier.padding(top = 28.dp),
                verticalArrangement = Arrangement.spacedBy(11.dp),
            ) {
                DeviceRow(name = DEVICE, sub = "Heart Rate Sensor · Bluetooth Classic", onPair = onPair)
                Text(
                    text = "Other devices".uppercase(),
                    color = OnixColors.Fg3,
                    fontFamily = SwitzerFontFamily,
                    fontSize = 11.sp,
                    letterSpacing = 1.5.sp,
                    modifier = Modifier.padding(top = 10.dp, bottom = 2.dp).padding(horizontal = 4.dp),
                )
                DeviceRow(name = "Other Device 1", muted = true)
                DeviceRow(name = "Other Device 2", muted = true)
            }
            Spacer(modifier = Modifier.weight(1f))
        },
        actions = {
            OnixButton(text = "Rescan", onClick = onRescan, variant = OnixButtonVariant.Outline, icon = "scan", fullWidth = true)
        },
    )
}

@Composable
private fun ConnectPairing(onCancel: () -> Unit, modifier: Modifier = Modifier) {
    ConnectShell(
        modifier = modifier,
        body = {
            ConnectCenter(gap = 28.dp) {
                PairingSpinner()
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = DEVICE, color = OnixColors.Fg1, fontFamily = EditorialFontFamily, fontWeight = FontWeight.W300, fontSize = 26.sp)
                    Text(
                        text = "Connecting…",
                        color = OnixColors.Fg2,
                        fontFamily = SwitzerFontFamily,
                        fontSize = 15.sp,
                        modifier = Modifier.padding(top = 4.dp),
                    )
                    Text(
                        text = "Bluetooth Classic · SPP · Just Works",
                        color = OnixColors.Fg3,
                        fontFamily = SwitzerFontFamily,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 10.dp),
                    )
                }
            }
        },
        actions = {
            ConnectTextButton(text = "Cancel", onClick = onCancel)
        },
    )
}

@Composable
private fun ConnectSuccess(onStartMonitoring: () -> Unit, modifier: Modifier = Modifier) {
    ConnectShell(
        modifier = modifier,
        body = {
            ConnectCenter {
                ResultCircle(ok = true)
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Connected!", color = OnixColors.Fg1, fontFamily = EditorialFontFamily, fontWeight = FontWeight.W300, fontSize = 40.sp)
                    Text(
                        text = "$DEVICE is ready",
                        color = OnixColors.Fg1,
                        fontFamily = SwitzerFontFamily,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(top = 8.dp),
                    )
                    Text(
                        text = "Your heart rate sensor is paired and ready to use.",
                        color = OnixColors.Fg3,
                        fontFamily = SwitzerFontFamily,
                        fontSize = 13.5.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp).widthIn(max = 270.dp),
                    )
                }
            }
        },
        actions = {
            OnixButton(text = "Start Monitoring", onClick = onStartMonitoring, icon = "heart", fullWidth = true)
        },
    )
}

@Composable
private fun ConnectFailed(onTryAgain: () -> Unit, onSkip: () -> Unit, modifier: Modifier = Modifier) {
    ConnectShell(
        modifier = modifier,
        body = {
            ConnectCenter {
                ResultCircle(ok = false)
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Connection\nFailed",
                        color = OnixColors.Fg1,
                        fontFamily = EditorialFontFamily,
                        fontWeight = FontWeight.W300,
                        fontSize = 40.sp,
                        textAlign = TextAlign.Center,
                    )
                    Text(
                        text = "Could not connect to $DEVICE. Make sure the device is powered on and within Bluetooth range.",
                        color = OnixColors.Fg2,
                        fontFamily = SwitzerFontFamily,
                        fontSize = 14.5.sp,
                        lineHeight = 21.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 12.dp).widthIn(max = 290.dp),
                    )
                }
            }
        },
        actions = {
            OnixButton(text = "Try Again", onClick = onTryAgain, icon = "scan", fullWidth = true)
            OnixButton(text = "Skip", onClick = onSkip, variant = OnixButtonVariant.Outline, fullWidth = true)
        },
    )
}
