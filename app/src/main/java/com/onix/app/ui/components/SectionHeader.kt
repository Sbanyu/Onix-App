package com.onix.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.onix.app.ui.theme.EditorialFontFamily
import com.onix.app.ui.theme.OnixColors
import com.onix.app.ui.theme.SwitzerFontFamily

/** Ported from kit.jsx's SectionHeader: a display-serif title with an optional muted meta label. */
@Composable
fun SectionHeader(title: String, meta: String? = null, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom,
    ) {
        Text(
            text = title,
            color = OnixColors.Fg1,
            fontFamily = EditorialFontFamily,
            fontWeight = FontWeight.W300,
            fontSize = 20.sp,
        )
        if (meta != null) {
            Text(
                text = meta,
                color = OnixColors.Fg2,
                fontFamily = SwitzerFontFamily,
                fontSize = 12.sp,
            )
        }
    }
}
