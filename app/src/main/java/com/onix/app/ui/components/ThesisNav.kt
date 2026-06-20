package com.onix.app.ui.components

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.onix.app.ui.icons.OnixIcon
import com.onix.app.ui.theme.OnixColors
import com.onix.app.ui.theme.SwitzerFontFamily

enum class ThesisTab { Home, Riwayat, Pengaturan, Profile, None }
enum class ThesisFabMode { Pinwheel, Stop }

/** Navigation callbacks shared by every post-auth, post-connect screen's [ThesisNav]. */
data class ThesisNavActions(
    val onHome: () -> Unit,
    val onRiwayat: () -> Unit,
    val onPengaturan: () -> Unit,
    val onProfile: () -> Unit,
    val onFab: () -> Unit,
)

private data class NavTab(val tab: ThesisTab, val icon: String, val label: String, val onClick: () -> Unit)

/**
 * Ported from screen_home_thesis.jsx's ThesisNav: the floating glass pill nav used by every
 * in-scope post-auth screen, with a contextual center FAB (brand pinwheel or stop square).
 */
@Composable
fun ThesisNav(
    actions: ThesisNavActions,
    active: ThesisTab = ThesisTab.Home,
    fabMode: ThesisFabMode = ThesisFabMode.Pinwheel,
    fabDisabled: Boolean = false,
    modifier: Modifier = Modifier,
) {
    val tabs = listOf(
        NavTab(ThesisTab.Home, "house", "Home", actions.onHome),
        NavTab(ThesisTab.Riwayat, "list", "Riwayat", actions.onRiwayat),
        NavTab(ThesisTab.Pengaturan, "gear", "Pengaturan", actions.onPengaturan),
        NavTab(ThesisTab.Profile, "user", "Profile", actions.onProfile),
    )
    val left = tabs.subList(0, 2)
    val right = tabs.subList(2, 4)
    val stop = fabMode == ThesisFabMode.Stop

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(100.dp))
            .background(Color.White.copy(alpha = 0.06f))
            .padding(6.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            left.forEach { NavTabButton(it, active, stop) }
            Box(contentAlignment = Alignment.Center) {
                if (stop) {
                    val transition = rememberInfiniteTransition(label = "fabPulse")
                    val progress by transition.animateFloat(
                        initialValue = 0f,
                        targetValue = 1f,
                        animationSpec = infiniteRepeatable(tween(durationMillis = 1600, easing = LinearOutSlowInEasing)),
                        label = "fabPulseRing",
                    )
                    Box(
                        modifier = Modifier
                            .size(58.dp)
                            .graphicsLayer {
                                scaleX = 1f + progress * 0.34f
                                scaleY = 1f + progress * 0.34f
                                alpha = (1f - progress) * 0.5f
                            }
                            .clip(RoundedCornerShape(100.dp))
                            .background(OnixColors.Red),
                    )
                }
                Box(
                    modifier = Modifier
                        .height(58.dp)
                        .width(58.dp)
                        .clip(RoundedCornerShape(100.dp))
                        .background(if (fabDisabled) OnixColors.BgTertiary else OnixColors.Red)
                        .clickable(enabled = !fabDisabled, onClick = actions.onFab),
                    contentAlignment = Alignment.Center,
                ) {
                    if (stop) {
                        OnixIcon(name = "stop", size = 20.dp, color = Color.White)
                    } else {
                        PinwheelMark(size = 26.dp, color = if (fabDisabled) OnixColors.Fg3 else Color.White)
                    }
                }
            }
            right.forEach { NavTabButton(it, active, stop) }
        }
    }
}

@Composable
private fun RowScope.NavTabButton(tab: NavTab, active: ThesisTab, stop: Boolean) {
    val isActive = active == tab.tab
    val color = if (isActive) OnixColors.Red else OnixColors.Fg2
    Box(
        modifier = Modifier
            .weight(1f)
            .height(58.dp)
            .clip(RoundedCornerShape(100.dp))
            .background(if (isActive) Color.White.copy(alpha = 0.09f) else Color.Transparent)
            .clickable(onClick = tab.onClick)
            .alpha(if (stop && !isActive) 0.4f else 1f),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            OnixIcon(name = tab.icon, size = 20.dp, color = color, weight = 2f)
            Text(text = tab.label, color = color, fontFamily = SwitzerFontFamily, fontSize = 11.sp)
        }
    }
}
