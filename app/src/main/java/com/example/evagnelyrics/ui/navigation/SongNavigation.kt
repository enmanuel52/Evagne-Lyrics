package com.example.evagnelyrics.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.evagnelyrics.ui.screen.list.ListScreen
import com.example.evagnelyrics.ui.screen.song.SongScreen
import kotlinx.serialization.Serializable

@Serializable
data object SongGraphDestination

@Serializable
data object SongListDestination

@Serializable
data class SongDestination(
    val title: String,
)

fun NavHostController.navigateToSongGraph(navOptions: NavOptions? = null) {
    navigate(SongGraphDestination, navOptions)
}

fun NavHostController.navigateToSong(title: String, navOptions: NavOptions? = null) {
    navigate(SongDestination(title), navOptions)
}

fun NavGraphBuilder.songsGraph(
    onBack: () -> Unit,
    onSong: (title: String) -> Unit
) {
    navigation<SongGraphDestination>(SongListDestination) {
        composable<SongListDestination> {
            ListScreen(onSong, onBack)
        }

        composable<SongDestination> {
            val destination = it.toRoute<SongDestination>()
            SongScreen(destination.title, onBack)
        }
    }
}