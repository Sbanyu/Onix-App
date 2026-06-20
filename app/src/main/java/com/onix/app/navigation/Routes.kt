package com.onix.app.navigation

/**
 * NavHost route keys, ported from index.html's `go()` screen keys. [SignUpScreen] and
 * [ConnectScreen] own their internal step sequence (name/goal/.../done and
 * scanning/found/.../success), so each gets only one route here; "home_nodevice" isn't a
 * route at all, since [HomeThesisScreen] renders both states itself based on its
 * `connected` parameter.
 */
object Routes {
    const val SignIn = "signin"

    const val SuName = "su_name"

    const val ConnectInitial = "c_initial"

    const val HomeThesis = "home_thesis"
    const val HomeLive = "home_live"
    const val HomeEnded = "home_ended"
    const val HomeHistory = "home_history"
    const val HomeSettings = "home_settings"
    const val HomeProfile = "home_profile"
    const val HomeEditProfile = "home_editprofile"
    const val HomePrivacy = "home_privacy"
    const val HomeAbout = "home_about"
}
