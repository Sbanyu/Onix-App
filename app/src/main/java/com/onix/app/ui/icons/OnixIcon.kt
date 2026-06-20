package com.onix.app.ui.icons

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Renders one glyph from [OnixIcons.map], parsing its raw SVG path data with [PathParser]
 * the same way Icon.jsx feeds `d` straight into an inline <path>. Scaling mirrors SVG's
 * default `preserveAspectRatio="xMidYMid meet"`: uniform scale, centered in the box.
 */
@Composable
fun OnixIcon(
    name: String,
    size: Dp = 20.dp,
    color: Color = Color.Unspecified,
    weight: Float = 2f,
    modifier: Modifier = Modifier,
) {
    val spec = OnixIcons.map[name] ?: return
    val path = remember(spec.path) { PathParser().parsePathString(spec.path).toPath() }
    val tint = color.takeIf { it != Color.Unspecified } ?: LocalContentColor.current

    Box(
        modifier = modifier
            .size(size)
            .drawBehind {
                val scale = minOf(this.size.width / spec.vbWidth, this.size.height / spec.vbHeight)
                val dx = (this.size.width - spec.vbWidth * scale) / 2f
                val dy = (this.size.height - spec.vbHeight * scale) / 2f

                translate(dx, dy) {
                    scale(scale, scale, pivot = Offset.Zero) {
                        when (spec.mode) {
                            IconMode.FILL, IconMode.FILL_RAW -> drawPath(path, color = tint, style = Fill)
                            IconMode.STROKE -> {
                                val strokeWidth = maxOf(2f, weight) * 8f
                                drawPath(
                                    path,
                                    color = tint,
                                    style = Stroke(
                                        width = strokeWidth,
                                        cap = StrokeCap.Round,
                                        join = StrokeJoin.Round,
                                    ),
                                )
                            }
                        }
                    }
                }
            },
    )
}
