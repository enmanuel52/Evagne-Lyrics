package com.example.evagnelyrics.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.evagnelyrics.ui.screen.main.MainScreen
import kotlinx.serialization.Serializable

@Serializable
data object HomeDestination

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = HomeDestination
    ) {
        composable<HomeDestination> {
            MainScreen { destination ->
                when (destination) {
                    TopDestination.Songs -> navController.navigateToSongGraph()
                    TopDestination.Gallery -> navController.navigateToGalleryGraph()
                }
            }
        }

        songsGraph(
            navController::popBackStack,
            navController::navigateToSong
        )

        galleryGraph(
            navController::popBackStack,
            navController::navigateToPicture
        )
    }

}