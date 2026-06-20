package com.onix.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.onix.app.screens.auth.SignInScreen
import com.onix.app.screens.auth.SignUpScreen
import com.onix.app.screens.connect.ConnectScreen
import com.onix.app.screens.home.HomeAboutScreen
import com.onix.app.screens.home.HomeEditProfileScreen
import com.onix.app.screens.home.HomeEndedScreen
import com.onix.app.screens.home.HomeHistoryScreen
import com.onix.app.screens.home.HomeLiveScreen
import com.onix.app.screens.home.HomePrivacyScreen
import com.onix.app.screens.home.HomeProfileScreen
import com.onix.app.screens.home.HomeSettingsScreen
import com.onix.app.screens.home.HomeThesisScreen
import com.onix.app.ui.components.ThesisNavActions

/**
 * Wires every [Routes] entry to its screen. Sign-up and connect steps own their internal
 * stepping, so each gets a single route; "home_nodevice" isn't a route at all here, since
 * [HomeThesisScreen] already renders both states itself based on [connected].
 */
@Composable
fun OnixNavGraph(navController: NavHostController = rememberNavController()) {
    var connected by rememberSaveable { mutableStateOf(false) }

    fun goToMain(connectedNow: Boolean) {
        connected = connectedNow
        navController.navigate(Routes.HomeThesis) {
            popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
        }
    }

    fun switchTab(route: String) {
        navController.navigate(route) {
            popUpTo(Routes.HomeThesis) { inclusive = false }
            launchSingleTop = true
        }
    }

    fun tabActions(fab: () -> Unit) = ThesisNavActions(
        onHome = { switchTab(Routes.HomeThesis) },
        onRiwayat = { switchTab(Routes.HomeHistory) },
        onPengaturan = { switchTab(Routes.HomeSettings) },
        onProfile = { switchTab(Routes.HomeProfile) },
        onFab = fab,
    )

    NavHost(navController = navController, startDestination = Routes.SignIn) {
        composable(Routes.SignIn) {
            SignInScreen(
                onSignIn = { goToMain(connectedNow = false) },
                onCreateAccount = { navController.navigate(Routes.SuName) },
            )
        }
        composable(Routes.SuName) {
            SignUpScreen(
                onBackToSignIn = { navController.popBackStack() },
                onConnectDevice = { navController.navigate(Routes.ConnectInitial) },
                onSkipToNoDevice = { goToMain(connectedNow = false) },
            )
        }
        composable(Routes.ConnectInitial) {
            ConnectScreen(
                onStartMonitoring = { goToMain(connectedNow = true) },
                onSkipToNoDevice = { goToMain(connectedNow = false) },
            )
        }
        composable(Routes.HomeThesis) {
            HomeThesisScreen(
                connected = connected,
                navActions = tabActions(fab = { navController.navigate(Routes.HomeLive) }),
                onConnectDevice = { navController.navigate(Routes.ConnectInitial) },
                onSeeAllTrend = { switchTab(Routes.HomeHistory) },
            )
        }
        composable(Routes.HomeLive) {
            HomeLiveScreen(navActions = tabActions(fab = { navController.navigate(Routes.HomeEnded) }))
        }
        composable(Routes.HomeEnded) {
            val backToHome = { navController.popBackStack(Routes.HomeThesis, false) }
            HomeEndedScreen(onDismiss = backToHome, onFinish = backToHome)
        }
        composable(Routes.HomeHistory) {
            HomeHistoryScreen(navActions = tabActions(fab = { navController.navigate(Routes.HomeLive) }))
        }
        composable(Routes.HomeSettings) {
            HomeSettingsScreen(
                navActions = tabActions(fab = { navController.navigate(Routes.HomeLive) }),
                onManageDevice = { navController.navigate(Routes.ConnectInitial) },
                onOpenPrivacy = { navController.navigate(Routes.HomePrivacy) },
                onOpenAbout = { navController.navigate(Routes.HomeAbout) },
                onSignOut = {
                    connected = false
                    navController.navigate(Routes.SignIn) { popUpTo(0) { inclusive = true } }
                },
            )
        }
        composable(Routes.HomeProfile) {
            HomeProfileScreen(
                navActions = tabActions(fab = { navController.navigate(Routes.HomeLive) }),
                onOpenSettings = { switchTab(Routes.HomeSettings) },
                onEditProfile = { navController.navigate(Routes.HomeEditProfile) },
                onOpenHistory = { switchTab(Routes.HomeHistory) },
            )
        }
        composable(Routes.HomeEditProfile) {
            HomeEditProfileScreen(
                onBack = { navController.popBackStack() },
                onSave = { navController.popBackStack() },
            )
        }
        composable(Routes.HomePrivacy) {
            HomePrivacyScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.HomeAbout) {
            HomeAboutScreen(
                onBack = { navController.popBackStack() },
                onOpenPrivacy = { navController.navigate(Routes.HomePrivacy) },
            )
        }
    }
}
