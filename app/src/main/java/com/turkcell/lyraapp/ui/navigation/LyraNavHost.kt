package com.turkcell.lyraapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.turkcell.lyraapp.ui.auth.login.LoginRoute
import com.turkcell.lyraapp.ui.auth.register.RegisterRoute
import com.turkcell.lyraapp.ui.home.HomeRoute

/**
 * Uygulamanın iskelet navigasyon yapısı.
 *
 * Tek [NavHost] Auth grafiğini ve Home ekranını barındırır; başlangıç hedefi
 * [LyraDestination.Login]'dir. MVI Effect'leri buradan sağlanan navigasyon
 * lambda'larına köprülenir (ViewModel navigasyon API'si bilmez; bkz. mvi-viewmodel-rules §6).
 */
@Composable
fun LyraNavHost(
    onToggleTheme: () -> Unit,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = LyraDestination.Login.route,
        modifier = modifier,
    ) {
        composable(LyraDestination.Login.route) {
            LoginRoute(
                onNavigateToHome = {
                    navController.navigate(LyraDestination.Home.route) {
                        popUpTo(LyraDestination.Login.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(LyraDestination.Register.route) {
                        launchSingleTop = true
                    }
                },
            )
        }

        composable(LyraDestination.Register.route) {
            RegisterRoute(
                onNavigateToHome = {
                    navController.navigate(LyraDestination.Home.route) {
                        popUpTo(LyraDestination.Login.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(LyraDestination.Login.route) {
                        popUpTo(LyraDestination.Login.route) { inclusive = false }
                        launchSingleTop = true
                    }
                },
                onNavigateBack = { navController.popBackStack() },
            )
        }

        composable(LyraDestination.Home.route) {
            HomeRoute(
                onToggleTheme = onToggleTheme,
                onNavigateToSearch = { /* TODO: Search ekranı eklenince bağlanacak. */ },
                onNavigateToLibrary = { /* TODO: Library ekranı eklenince bağlanacak. */ },
                onNavigateToFavorites = { /* TODO: Favorites ekranı eklenince bağlanacak. */ },
                onNavigateToProfile = { /* TODO: Profile ekranı eklenince bağlanacak. */ },
            )
        }
    }
}
