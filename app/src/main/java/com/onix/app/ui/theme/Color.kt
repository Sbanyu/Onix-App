package com.onix.app.ui.theme

import androidx.compose.ui.graphics.Color

/** Design tokens ported 1:1 from project/colors_and_type.css (dark mode — the only mode this app ships). */
object OnixColors {
    // brand red ramp
    val Red = Color(0xFFEF4444)
    val RedHover = Color(0xFFDC2626)
    val RedPress = Color(0xFFB91C1C)
    val RedSoft = Color(0xFFF87171)
    val RedTint = Color(0xFFFCA5A5)
    val RedGlow = Color(0xE6F19797) // rgba(241,151,151,.90)
    val RedShadow = Color(0x40FB8686) // rgba(251,134,134,.25)

    // semantic
    val Success = Color(0xFF16B364)
    val Warning = Color(0xFFEF6820)
    val Info = Color(0xFF2E90FA)
    val Error = Color(0xFFEF4444)

    // neutrals / surfaces (dark)
    val Black = Color(0xFF0A0A0A)
    val White = Color(0xFFFFFFFF)
    val BgPrimary = Color(0xFF0A0A0A)
    val BgSecondary = Color(0xFF171717)
    val BgTertiary = Color(0xFF262626)
    val BgElevated = Color(0xFF1F1F1F)

    val Fg1 = Color(0xFFF5F5F5)
    val Fg2 = Color(0xFF94A3B8)
    val Fg3 = Color(0xFF64748B)
    val FgPlaceholder = Color(0xFF64748B)
    val FgOnAccent = Color(0xFFFFFFFF)

    val Border1 = Color(0x14FFFFFF) // rgba(255,255,255,.08)
    val Border2 = Color(0x24FFFFFF) // rgba(255,255,255,.14)
    val BorderStrong = Color(0xFF334155)

    val ShadowCard = Color(0x66000000)
    val ShadowPop = Color(0x8C000000)
}

/**
 * Several thesis/onboarding screens use slightly different literal reds than [OnixColors.Red]
 * (a quirk preserved from the original design files, not a typo). Keeping both faithfully
 * reproduces the prototype rather than silently "fixing" it.
 */
object ThesisColors {
    val Red = Color(0xFFE84040) // SU_RED / HT_RED in the prototype
    val RedTint = Color(0x1AE84040) // rgba(232,64,64,.10)
    val Green = Color(0xFF1D9E75)
    val GreenBg = Color(0x1F1D9E75) // rgba(29,158,117,.12)
    val Purple = Color(0xFF7F77DD)
    val PurpleBg = Color(0x247F77DD) // rgba(127,119,221,.14)
    val Amber = Color(0xFFD97706)
    val AmberBg = Color(0x24D97706) // rgba(217,119,6,.14)
    val Slate = Color(0xFF1E293B)
}
