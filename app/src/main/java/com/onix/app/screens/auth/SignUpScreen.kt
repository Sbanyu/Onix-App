package com.onix.app.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.weight
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.onix.app.ui.components.LogoDisc
import com.onix.app.ui.components.OnixButton
import com.onix.app.ui.components.OnixField
import com.onix.app.ui.icons.OnixIcon
import com.onix.app.ui.theme.EditorialFontFamily
import com.onix.app.ui.theme.OnixColors
import com.onix.app.ui.theme.SwitzerFontFamily
import com.onix.app.ui.theme.ThesisColors
import com.onix.app.ui.theme.insetTopGlow
import kotlin.math.abs
import kotlin.math.roundToInt

private enum class SignUpStep { Name, Goal, About, Height, Weight, Account, Done }

/**
 * Ported from screen_signup.jsx: 6-step onboarding (name/goal/about/height/weight/account) plus
 * the Congratulations screen, mirroring the original's single-component, locally-stepped design.
 * Unit toggles (cm/ft·in, Kg/Lbs) only relabel the same number, exactly as in the prototype —
 * a preserved quirk, not a bug.
 */
@Composable
fun SignUpScreen(
    onBackToSignIn: () -> Unit,
    onConnectDevice: () -> Unit,
    onSkipToNoDevice: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var step by remember { mutableStateOf(SignUpStep.Name) }
    var name by remember { mutableStateOf("") }
    var goals by remember { mutableStateOf(setOf("sehari")) }
    var sex by remember { mutableStateOf("male") }
    var age by remember { mutableStateOf("") }
    var heightUnit by remember { mutableStateOf("cm") }
    var height by remember { mutableStateOf(165) }
    var weightUnit by remember { mutableStateOf("Kg") }
    var weight by remember { mutableStateOf(72) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }

    val firstName = name.trim().split(" ").firstOrNull()?.takeIf { name.trim().isNotEmpty() } ?: "Jhon"

    fun toggleGoal(k: String) {
        goals = if (goals.contains(k)) goals - k else if (goals.size >= 3) goals else goals + k
    }

    when (step) {
        SignUpStep.Name -> SuShell(onBack = onBackToSignIn, onNext = { step = SignUpStep.Goal }, step = 0) {
            SuTitle("Pertama, Mari mulai dengan nama Anda.")
            Box(modifier = Modifier.padding(top = 36.dp)) {
                SuInput(label = "First Name", placeholder = "e.g. Jhon", value = name, onChange = { name = it })
            }
        }

        SignUpStep.Goal -> SuShell(onBack = { step = SignUpStep.Name }, onNext = { step = SignUpStep.About }, step = 1) {
            SuTitle("Halo, $firstName. 👋 Apa tujuan monitoring anda?")
            Text(
                text = "Select up to three that are most important to you.",
                color = OnixColors.Fg2,
                fontFamily = SwitzerFontFamily,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 16.dp),
            )
            Column(
                modifier = Modifier.padding(top = 18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                val items = listOf(
                    "sehari" to "Pantau kesehatan sehari-hari",
                    "olahraga" to "Teman olahraga",
                    "aritmia" to "Deteksi aritmia dini",
                    "riset" to "Riset & data ilmiah",
                )
                items.forEach { (key, label) ->
                    SuCheckRow(label = label, checked = goals.contains(key), onToggle = { toggleGoal(key) })
                }
            }
        }

        SignUpStep.About -> SuShell(onBack = { step = SignUpStep.Goal }, onNext = { step = SignUpStep.Height }, step = 2) {
            SuTitle("Beri tahu sedikit tentang diri Anda")
            Text(
                text = "Please select which sex we should use to calculate your calorie needs",
                color = OnixColors.Fg2,
                fontFamily = SwitzerFontFamily,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                modifier = Modifier.padding(top = 18.dp),
            )
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                SuRadioPill(label = "Laki – Laki", active = sex == "male", onClick = { sex = "male" }, modifier = Modifier.weight(1f))
                SuRadioPill(label = "Perempuan", active = sex == "female", onClick = { sex = "female" }, modifier = Modifier.weight(1f))
            }
            Box(modifier = Modifier.padding(top = 22.dp)) {
                SuInput(label = "Berapa umur kamu?", placeholder = "e.g. 20", value = age, onChange = { age = it }, keyboardType = KeyboardType.Number)
            }
        }

        SignUpStep.Height -> SuShell(onBack = { step = SignUpStep.About }, onNext = { step = SignUpStep.Weight }, step = 3) {
            SuTitle("Berapa tinggi badan Anda?")
            Box(modifier = Modifier.padding(top = 22.dp)) {
                SuSegmented(options = listOf("cm", "ft/in"), value = heightUnit, onChange = { heightUnit = it })
            }
            Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                SuWheel(value = height, onValueChange = { height = it }, suffix = heightUnit)
            }
        }

        SignUpStep.Weight -> SuShell(onBack = { step = SignUpStep.Height }, onNext = { step = SignUpStep.Account }, step = 4) {
            SuTitle("Berapa berat badan Anda?")
            Box(modifier = Modifier.padding(top = 22.dp)) {
                SuSegmented(options = listOf("Kg", "Lbs"), value = weightUnit, onChange = { weightUnit = it })
            }
            Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                SuRuler(value = weight, onValueChange = { weight = it }, min = 40, max = 150, suffix = weightUnit)
            }
        }

        SignUpStep.Account -> SuShell(onBack = { step = SignUpStep.Weight }, onNext = { step = SignUpStep.Done }, step = 5) {
            SuTitle("Hampir selesai! Buat akun kamu")
            Column(
                modifier = Modifier.padding(top = 34.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp),
            ) {
                SuInput(label = "Email", placeholder = "Enter your email address", value = email, onChange = { email = it }, keyboardType = KeyboardType.Email)
                Column {
                    Text(text = "Password", color = OnixColors.Fg2, fontFamily = SwitzerFontFamily, fontSize = 14.sp, modifier = Modifier.padding(bottom = 10.dp))
                    OnixField(
                        value = password,
                        onValueChange = { password = it },
                        placeholder = "Enter your password",
                        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        trailing = { SuEyeToggle(shown = showPassword, onToggle = { showPassword = !showPassword }) },
                    )
                }
                Column {
                    Text(text = "Confirm Password", color = OnixColors.Fg2, fontFamily = SwitzerFontFamily, fontSize = 14.sp, modifier = Modifier.padding(bottom = 10.dp))
                    OnixField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        placeholder = "Confirm your password",
                        visualTransformation = if (showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        trailing = { SuEyeToggle(shown = showConfirmPassword, onToggle = { showConfirmPassword = !showConfirmPassword }) },
                    )
                }
            }
        }

        SignUpStep.Done -> SuCongratulations(
            firstName = firstName,
            onConnectDevice = onConnectDevice,
            onSkipToNoDevice = onSkipToNoDevice,
            modifier = modifier,
        )
    }
}

@Composable
private fun SuEyeToggle(shown: Boolean, onToggle: () -> Unit) {
    Box(
        modifier = Modifier.size(24.dp).clickable(onClick = onToggle),
        contentAlignment = Alignment.Center,
    ) {
        OnixIcon(name = "eye", size = 18.dp, color = OnixColors.Fg3)
    }
}

/** Warm red top glow, richer than the default frame glow — used on every signup/connect screen. */
@Composable
private fun SuGlow(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(340.dp)
            .background(
                Brush.verticalGradient(
                    0f to Color(0x57E84040),
                    0.2f to Color(0x29781616),
                    0.42f to Color(0x000A0A0A),
                ),
            ),
    )
}

@Composable
private fun SuTop(step: Int, total: Int, onBack: () -> Unit) {
    Column(modifier = Modifier.padding(top = 6.dp)) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.07f))
                .clickable(onClick = onBack),
            contentAlignment = Alignment.Center,
        ) {
            OnixIcon(name = "caretLeft", size = 19.dp, color = OnixColors.Fg1)
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 22.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            repeat(total) { i ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(5.dp)
                        .clip(RoundedCornerShape(999.dp))
                        .background(if (i <= step) ThesisColors.Red else Color.White.copy(alpha = 0.13f)),
                )
            }
        }
    }
}

@Composable
private fun SuTitle(text: String) {
    Text(
        text = text,
        color = OnixColors.Fg1,
        fontFamily = EditorialFontFamily,
        fontWeight = FontWeight.W300,
        fontSize = 40.sp,
        lineHeight = 45.sp,
        letterSpacing = (-0.4).sp,
        modifier = Modifier.padding(top = 30.dp),
    )
}

@Composable
private fun SuInput(
    label: String?,
    placeholder: String,
    value: String,
    onChange: (String) -> Unit,
    icon: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    Column {
        if (label != null) {
            Text(text = label, color = OnixColors.Fg2, fontFamily = SwitzerFontFamily, fontSize = 14.sp, modifier = Modifier.padding(bottom = 10.dp))
        }
        OnixField(value = value, onValueChange = onChange, icon = icon, placeholder = placeholder, keyboardType = keyboardType)
    }
}

@Composable
private fun SuCheckRow(label: String, checked: Boolean, onToggle: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(OnixColors.BgSecondary)
            .border(1.dp, if (checked) ThesisColors.Red else OnixColors.Border1, RoundedCornerShape(14.dp))
            .clickable(onClick = onToggle)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            color = OnixColors.Fg1,
            fontFamily = SwitzerFontFamily,
            fontWeight = FontWeight.W500,
            fontSize = 15.5.sp,
            modifier = Modifier.weight(1f),
        )
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(RoundedCornerShape(7.dp))
                .background(if (checked) ThesisColors.Red else Color.Transparent)
                .border(1.5.dp, if (checked) ThesisColors.Red else OnixColors.Border2, RoundedCornerShape(7.dp)),
            contentAlignment = Alignment.Center,
        ) {
            if (checked) {
                OnixIcon(name = "check", size = 15.dp, color = Color.White, weight = 2.4f)
            }
        }
    }
}

@Composable
private fun SuRadioPill(label: String, active: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(if (active) ThesisColors.RedTint else OnixColors.BgSecondary)
            .border(1.dp, if (active) ThesisColors.Red else OnixColors.Border1, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 15.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            color = OnixColors.Fg1,
            fontFamily = SwitzerFontFamily,
            fontWeight = FontWeight.W500,
            fontSize = 15.sp,
            modifier = Modifier.weight(1f),
        )
        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(CircleShape)
                .border(2.dp, if (active) ThesisColors.Red else OnixColors.Border2, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            if (active) {
                Box(modifier = Modifier.size(11.dp).clip(CircleShape).background(ThesisColors.Red))
            }
        }
    }
}

@Composable
private fun SuSegmented(options: List<String>, value: String, onChange: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(OnixColors.BgSecondary)
            .padding(5.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        options.forEach { option ->
            val selected = value == option
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
                    .clip(RoundedCornerShape(11.dp))
                    .background(if (selected) ThesisColors.Red else Color.Transparent)
                    .let { if (selected) it.insetTopGlow(OnixColors.RedGlow, height = 6.dp) else it }
                    .clickable { onChange(option) },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = option,
                    color = if (selected) Color.White else OnixColors.Fg2,
                    fontFamily = SwitzerFontFamily,
                    fontWeight = FontWeight.W600,
                    fontSize = 15.sp,
                )
            }
        }
    }
}

@Composable
private fun SuWheel(value: Int, onValueChange: (Int) -> Unit, suffix: String) {
    val offsets = listOf(-2, -1, 0, 1, 2)
    Column(
        modifier = Modifier.padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        offsets.forEach { o ->
            val center = o == 0
            val v = value + o
            val fontSize = if (center) 56.sp else if (abs(o) == 1) 34.sp else 26.sp
            val color = if (center) OnixColors.Fg1 else if (abs(o) == 1) Color(0xD994A3B8) else Color(0x9964748B)
            Box(
                modifier = Modifier
                    .let {
                        if (center) {
                            it
                                .clip(RoundedCornerShape(999.dp))
                                .border(1.dp, ThesisColors.Red, RoundedCornerShape(999.dp))
                                .padding(vertical = 14.dp, horizontal = 56.dp)
                        } else {
                            it.clickable { onValueChange(value + o) }.padding(vertical = 4.dp)
                        }
                    },
                contentAlignment = Alignment.Center,
            ) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(text = "$v", color = color, fontFamily = EditorialFontFamily, fontWeight = FontWeight.W300, fontSize = fontSize, lineHeight = fontSize)
                    if (center) {
                        Text(text = " $suffix", color = OnixColors.Fg3, fontFamily = SwitzerFontFamily, fontSize = 16.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun SuRuler(value: Int, onValueChange: (Int) -> Unit, min: Int, max: Int, suffix: String) {
    val spacingDp = 13f
    val density = LocalDensity.current
    var dragAccumPx by remember { mutableStateOf(0f) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.Top) {
            Text(text = "$value", color = OnixColors.Fg1, fontFamily = EditorialFontFamily, fontWeight = FontWeight.W300, fontSize = 64.sp, lineHeight = 64.sp)
            Text(text = " $suffix", color = OnixColors.Fg3, fontFamily = SwitzerFontFamily, fontSize = 16.sp, modifier = Modifier.padding(top = 12.dp))
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(92.dp)
                .padding(top = 20.dp)
                .pointerInput(min, max) {
                    detectDragGestures(
                        onDragStart = { dragAccumPx = 0f },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            dragAccumPx += dragAmount.x
                            val spacingPx = with(density) { spacingDp.dp.toPx() }
                            val deltaUnits = -(dragAccumPx / spacingPx).roundToInt()
                            if (deltaUnits != 0) {
                                val newValue = (value + deltaUnits).coerceIn(min, max)
                                onValueChange(newValue)
                                dragAccumPx += deltaUnits * spacingPx
                            }
                        },
                    )
                },
        ) {
            val ticks = (value - 20..value + 20).filter { it in min..max }
            ticks.forEach { i ->
                val major = i % 5 == 0
                val offsetDp = (i - value) * spacingDp
                Box(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .offset(x = offsetDp.dp, y = 0.dp)
                        .align(Alignment.TopCenter)
                        .width(if (major) 2.dp else 1.5.dp)
                        .height(if (major) 34.dp else 18.dp)
                        .background(if (major) Color(0xBFCBD5E1) else Color(0x7394A3B8)),
                )
                if (major) {
                    Text(
                        text = "$i",
                        color = OnixColors.Fg3,
                        fontFamily = SwitzerFontFamily,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .offset(x = offsetDp.dp, y = 58.dp),
                    )
                }
            }
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = 6.dp)
                    .width(3.dp)
                    .height(54.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(ThesisColors.Red),
            )
        }
    }
}

@Composable
private fun SuShell(
    onBack: () -> Unit,
    onNext: () -> Unit,
    step: Int,
    nextLabel: String = "Next",
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize()) {
            SuGlow()
            Column(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 28.dp),
                ) {
                    SuTop(step = step, total = 6, onBack = onBack)
                    content()
                }
                Box(modifier = Modifier.padding(horizontal = 28.dp).padding(top = 12.dp, bottom = 30.dp)) {
                    OnixButton(text = nextLabel, onClick = onNext, fullWidth = true)
                }
            }
        }
    }
}

@Composable
private fun SuCongratulations(
    firstName: String,
    onConnectDevice: () -> Unit,
    onSkipToNoDevice: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        SuGlow()
        Column(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
            Column(
                modifier = Modifier.weight(1f).fillMaxWidth().padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Box(
                        modifier = Modifier
                            .size(220.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    0f to Color(0x6BE84040),
                                    0.65f to Color(0x00E84040),
                                ),
                            ),
                    )
                    LogoDisc(size = 104.dp, radius = 30.dp)
                }
                Text(
                    text = "Semua siap,\n$firstName",
                    color = OnixColors.Fg1,
                    fontFamily = EditorialFontFamily,
                    fontWeight = FontWeight.W300,
                    fontSize = 44.sp,
                    lineHeight = 48.sp,
                    modifier = Modifier.padding(top = 40.dp),
                )
                Text(
                    text = "Akun Onix kamu sudah siap. Hubungkan sensor detak jantung untuk mulai monitoring.",
                    color = OnixColors.Fg2,
                    fontFamily = SwitzerFontFamily,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 14.dp),
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 28.dp).padding(bottom = 30.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                OnixButton(text = "Hubungkan Perangkat", onClick = onConnectDevice, icon = "arrowRight", fullWidth = true)
                Text(
                    text = "Nanti saja",
                    color = OnixColors.Fg3,
                    fontFamily = SwitzerFontFamily,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onSkipToNoDevice)
                        .padding(6.dp),
                )
            }
        }
    }
}
