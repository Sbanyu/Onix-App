package com.onix.app.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.onix.app.ui.theme.OnixColors

/** Ported from kit.jsx's Avatar: a circular crop with an optional red ring border. */
@Composable
fun Avatar(
    @DrawableRes src: Int? = null,
    size: Dp = 48.dp,
    ring: Boolean = false,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(OnixColors.Black)
            .let {
                if (ring) it.border(2.dp, OnixColors.Red, CircleShape) else it
            },
    ) {
        if (src != null) {
            Image(
                painter = painterResource(id = src),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(size),
            )
        }
    }
}
