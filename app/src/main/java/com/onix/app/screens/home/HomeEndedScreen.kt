package com.onix.app.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.onix.app.ui.components.OnixButton
import com.onix.app.ui.components.OnixButtonVariant
import com.onix.app.ui.components.StatusPill
import com.onix.app.ui.icons.OnixIcon
import com.onix.app.ui.theme.EditorialFontFamily
import com.onix.app.ui.theme.OnixColors
import com.onix.app.ui.theme.SwitzerFontFamily
import com.onix.app.ui.theme.ThesisColors

/**
 * Ported from screen_home_thesis.jsx's HomeEndedScreen (25 · Session Ended): a frozen
 * [HomeLiveBody] backdrop behind a dim scrim and a bottom sheet summarizing the session.
 * "Simpan ke Riwayat" and "Buang" both call [onFinish], mirroring the source's identical
 * `go("home_thesis")` handler for both buttons (no real persistence layer exists yet).
 */
@Composable
fun HomeEndedScreen(
    onDismiss: () -> Unit,
    onFinish: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
            Box(modifier = Modifier.weight(1f).padding(bottom = 24.dp).clipToBounds()) {
                HomeLiveBody(sec = 522, bpm = 76, frozen = true)
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.6f))
                .clickable(onClick = onDismiss),
        )
        SessionEndedSheet(
            onDismiss = onDismiss,
            onFinish = onFinish,
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}

@Composable
private fun SessionEndedSheet(onDismiss: () -> Unit, onFinish: () -> Unit, modifier: Modifier = Modifier) {
    var note by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(OnixColors.BgSecondary)
            .padding(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 26.dp),
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 18.dp)
                .width(40.dp)
                .height(4.dp)
                .clip(RoundedCornerShape(999.dp))
                .background(OnixColors.Border2),
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = "Sesi Selesai", color = OnixColors.Fg1, fontFamily = EditorialFontFamily, fontWeight = FontWeight.W300, fontSize = 28.sp)
            Box(modifier = Modifier.clickable(onClick = onDismiss).padding(4.dp)) {
                OnixIcon(name = "x", size = 20.dp, color = OnixColors.Fg2, weight = 2f)
            }
        }
        Box(modifier = Modifier.fillMaxWidth().padding(vertical = 15.dp).height(1.dp).background(OnixColors.Border1))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            HtSheetStat(label = "Durasi", value = "08:42", unit = "menit", modifier = Modifier.weight(1f))
            HtSheetStat(label = "Rata-rata", value = "76", unit = "BPM", modifier = Modifier.weight(1f))
        }
        Row(modifier = Modifier.fillMaxWidth().padding(top = 10.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            HtSheetStat(label = "Minimum", value = "62", unit = "BPM", modifier = Modifier.weight(1f))
            HtSheetStat(label = "Maksimum", value = "89", unit = "BPM", modifier = Modifier.weight(1f))
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = "Status Kesehatan:", color = OnixColors.Fg2, fontFamily = SwitzerFontFamily, fontSize = 13.sp)
            StatusPill(text = "NORMAL", color = ThesisColors.Green, background = ThesisColors.GreenBg)
        }
        Text(
            text = "Moving Average Filter · N = 10 · Rabu, 26 Okt 2025 · 09:41",
            color = OnixColors.Fg3,
            fontFamily = SwitzerFontFamily,
            fontSize = 11.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
        )
        NoteField(value = note, onValueChange = { note = it }, modifier = Modifier.fillMaxWidth().padding(top = 14.dp))
        OnixButton(
            text = "Simpan ke Riwayat",
            onClick = onFinish,
            fullWidth = true,
            modifier = Modifier.padding(top = 14.dp),
        )
        OnixButton(
            text = "Buang",
            onClick = onFinish,
            variant = OnixButtonVariant.Outline,
            fullWidth = true,
            modifier = Modifier.padding(top = 10.dp),
        )
    }
}

@Composable
private fun HtSheetStat(label: String, value: String, unit: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(OnixColors.BgTertiary)
            .padding(vertical = 11.dp, horizontal = 13.dp),
    ) {
        Text(text = label, color = OnixColors.Fg2, fontFamily = SwitzerFontFamily, fontSize = 11.sp)
        Row(modifier = Modifier.padding(top = 3.dp), verticalAlignment = Alignment.Bottom) {
            Text(text = value, color = OnixColors.Fg1, fontFamily = EditorialFontFamily, fontWeight = FontWeight.W300, fontSize = 26.sp)
            Text(text = " $unit", color = OnixColors.Fg3, fontFamily = SwitzerFontFamily, fontSize = 12.sp)
        }
    }
}

@Composable
private fun NoteField(value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(OnixColors.BgPrimary)
            .border(1.dp, OnixColors.Border1, RoundedCornerShape(12.dp))
            .padding(13.dp),
    ) {
        if (value.isEmpty()) {
            Text(
                text = "Tambahkan catatan... (opsional)",
                color = OnixColors.FgPlaceholder,
                fontFamily = SwitzerFontFamily,
                fontSize = 14.sp,
            )
        }
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(color = OnixColors.Fg1, fontFamily = SwitzerFontFamily, fontSize = 14.sp),
            singleLine = true,
            cursorBrush = SolidColor(OnixColors.Red),
        )
    }
}
