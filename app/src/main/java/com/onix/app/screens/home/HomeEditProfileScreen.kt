package com.onix.app.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.onix.app.R
import com.onix.app.ui.components.Avatar
import com.onix.app.ui.components.OnixButton
import com.onix.app.ui.components.OnixField
import com.onix.app.ui.components.SubPage
import com.onix.app.ui.icons.OnixIcon
import com.onix.app.ui.theme.OnixColors
import com.onix.app.ui.theme.SwitzerFontFamily

/**
 * Ported from screen_settings.jsx's HomeEditProfileScreen (29 · Edit Profil): an avatar with
 * an edit badge over a column of [EpField]s bound to local draft state.
 */
@Composable
fun HomeEditProfileScreen(onBack: () -> Unit, onSave: () -> Unit, modifier: Modifier = Modifier) {
    var name by remember { mutableStateOf("Brian Wijaya") }
    var email by remember { mutableStateOf("brian@onix.app") }
    var age by remember { mutableStateOf("27") }
    var weight by remember { mutableStateOf("72") }
    var height by remember { mutableStateOf("178") }

    SubPage(
        title = "Edit Profil",
        onBack = onBack,
        modifier = modifier,
        footer = { OnixButton(text = "Simpan Perubahan", onClick = onSave, fullWidth = true) },
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box {
                Avatar(src = R.drawable.avatar_brian, size = 92.dp, ring = true)
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = 2.dp, y = 2.dp)
                        .size(32.dp)
                        .clip(CircleShape)
                        .border(3.dp, OnixColors.BgPrimary, CircleShape)
                        .background(OnixColors.Red),
                    contentAlignment = Alignment.Center,
                ) {
                    OnixIcon(name = "camera", size = 15.dp, color = OnixColors.White, weight = 2f)
                }
            }
            Text(
                text = "Ubah Foto",
                color = OnixColors.Red,
                fontFamily = SwitzerFontFamily,
                fontWeight = FontWeight.W600,
                fontSize = 13.sp,
                modifier = Modifier.padding(top = 12.dp),
            )
        }
        Column(
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp, start = 24.dp, end = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            EpField(label = "Nama Lengkap", value = name, onValueChange = { name = it })
            EpField(label = "Email", value = email, onValueChange = { email = it })
            EpField(label = "Umur", value = age, onValueChange = { age = it }, suffix = "tahun")
            EpField(label = "Berat", value = weight, onValueChange = { weight = it }, suffix = "kg")
            EpField(label = "Tinggi", value = height, onValueChange = { height = it }, suffix = "cm")
        }
    }
}

@Composable
private fun EpField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    suffix: String? = null,
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            color = OnixColors.Fg2,
            fontFamily = SwitzerFontFamily,
            fontSize = 13.sp,
            modifier = Modifier.padding(start = 2.dp, bottom = 9.dp),
        )
        OnixField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            trailing = if (suffix != null) {
                { Text(text = suffix, color = OnixColors.Fg3, fontFamily = SwitzerFontFamily, fontSize = 14.sp) }
            } else {
                null
            },
        )
    }
}
