package com.onix.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private const val BLADE_PATH =
    "M 18.064 0 L 24.312 2.888 L 23.512 10.068 L 12.343 22.724 " +
        "C 5.86 18.55 1.239 11.735 0 3.799 L 18.064 0 Z"
private val BLADE_ANGLES = listOf(0f, 60f, 120f, 180f, 240f, 300f)
private const val VIEWBOX = 56f

/**
 * Onix brand pinwheel, ported 1:1 from kit.jsx's PinwheelMark. Each of the six blades is the
 * same path under `translate(14 12.5)` -> `rotate(d, pivot 14 15.5)` -> `translate(10 0)` ->
 * `scale(0.5)`, kept as separate nested transform calls (mirroring the SVG's nested <g>s) so
 * the combined-transform order matches exactly rather than being collapsed into one matrix.
 */
@Composable
fun PinwheelMark(size: Dp = 26.dp, color: Color = Color.White, modifier: Modifier = Modifier) {
    val bladePath = remember { PathParser().parsePathString(BLADE_PATH).toPath() }
    Canvas(modifier = modifier.size(size)) {
        val scaleFactor = this.size.minDimension / VIEWBOX
        scale(scaleFactor, pivot = Offset.Zero) {
            translate(left = 14f, top = 12.5f) {
                for (angle in BLADE_ANGLES) {
                    rotate(degrees = angle, pivot = Offset(14f, 15.5f)) {
                        translate(left = 10f, top = 0f) {
                            scale(0.5f, pivot = Offset.Zero) {
                                drawPath(bladePath, color = color)
                            }
                        }
                    }
                }
            }
        }
    }
}
