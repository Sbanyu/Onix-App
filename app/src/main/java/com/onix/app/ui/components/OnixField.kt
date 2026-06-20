package com.onix.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.onix.app.ui.icons.OnixIcon
import com.onix.app.ui.theme.OnixColors
import com.onix.app.ui.theme.OnixRadius
import com.onix.app.ui.theme.SwitzerFontFamily

/** Ported from kit.jsx's Field: a pill text input with leading icon, focus ring, and a trailing slot. */
@Composable
fun OnixField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    icon: String? = null,
    placeholder: String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailing: (@Composable () -> Unit)? = null,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val focused by interactionSource.collectIsFocusedAsState()
    val borderColor = if (focused) OnixColors.Red else OnixColors.Border1
    val iconColor = if (focused) OnixColors.Red else OnixColors.Fg3

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(OnixRadius.full))
            .background(OnixColors.BgSecondary)
            .border(1.dp, borderColor, RoundedCornerShape(OnixRadius.full))
            .padding(vertical = 15.dp, horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (icon != null) {
            OnixIcon(name = icon, size = 18.dp, color = iconColor, weight = 2f)
            Box(modifier = Modifier.width(11.dp))
        }
        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
            if (value.isEmpty()) {
                Text(
                    text = placeholder,
                    color = OnixColors.FgPlaceholder,
                    fontFamily = SwitzerFontFamily,
                    fontSize = 15.sp,
                )
            }
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(
                    color = OnixColors.Fg1,
                    fontFamily = SwitzerFontFamily,
                    fontSize = 15.sp,
                ),
                singleLine = true,
                cursorBrush = SolidColor(OnixColors.Red),
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                visualTransformation = visualTransformation,
                interactionSource = interactionSource,
            )
        }
        if (trailing != null) {
            trailing()
        }
    }
}
