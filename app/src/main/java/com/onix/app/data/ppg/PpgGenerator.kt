package com.onix.app.data.ppg

import kotlin.math.exp
import kotlin.math.pow
import kotlin.math.sin

/** Deterministic mulberry32 PRNG, ported 1:1 from screen_home_thesis.jsx's `htMulberry`. */
private fun mulberry32(seed: Int): () -> Double {
    var a = seed
    return {
        a += 0x6D2B79F5
        var t = (a xor (a ushr 15)) * (a or 1)
        t = (t + ((t xor (t ushr 7)) * (t or 61))) xor t
        ((t xor (t ushr 14)).toLong() and 0xFFFFFFFFL).toDouble() / 4294967296.0
    }
}

/** Normalized (0..1) raw/filtered PPG sample arrays for one repeating heartbeat-window tile. */
data class PpgWaveform(
    val rawNorm: FloatArray,
    val filteredNorm: FloatArray,
    val sampleCount: Int,
    val tileWidth: Float,
    val tileHeight: Float,
)

/**
 * Synthesizes a fake photoplethysmography trace, ported 1:1 from screen_home_thesis.jsx's
 * `htGenPPG`: a Gaussian-bump heartbeat shape plus PRNG noise (raw), and a trailing
 * moving-average of window N=10 (filtered) — the same two lines drawn in the Live screen.
 */
fun generatePpg(seed: Int = 7): PpgWaveform {
    val rnd = mulberry32(seed)
    val samples = 170
    val beats = 6
    val width = 322f
    val height = 92f
    val n = 10

    val clean = DoubleArray(samples) { i ->
        val ph = ((i.toDouble() / samples) * beats) % 1.0
        var v = exp(-((ph - 0.13) / 0.045).pow(2))
        v += 0.34 * exp(-((ph - 0.44) / 0.085).pow(2))
        v
    }
    val raw = DoubleArray(samples) { i -> clean[i] + (rnd() - 0.5) * 0.22 + 0.05 * sin(i / 6.5) }
    val filt = DoubleArray(samples) { i ->
        var s = 0.0
        var c = 0
        for (k in -n + 1..0) {
            val j = i + k
            if (j >= 0) {
                s += raw[j]
                c++
            }
        }
        s / c
    }
    val all = raw + filt
    val min = all.min()
    val max = all.max()
    val range = (max - min).let { if (it == 0.0) 1.0 else it }

    return PpgWaveform(
        rawNorm = FloatArray(samples) { i -> ((raw[i] - min) / range).toFloat() },
        filteredNorm = FloatArray(samples) { i -> ((filt[i] - min) / range).toFloat() },
        sampleCount = samples,
        tileWidth = width,
        tileHeight = height,
    )
}
