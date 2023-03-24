package com.example.evagnelyrics.ui.navigation

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.evagnelyrics.core.LocalNavController
import com.example.evagnelyrics.ui.screen.gallery.GalleryScreen
import com.example.evagnelyrics.ui.screen.list.ListScreen
import com.example.evagnelyrics.ui.screen.main.MainScreen
import com.example.evagnelyrics.ui.screen.picture.PictureScreen
import com.example.evagnelyrics.ui.screen.song.SongScreen

@Composable
fun AppNavigation(keepSplash: () -> Unit) {
    val navController = rememberNavController()
    CompositionLocalProvider(values = arrayOf(LocalNavController provides navController)) {
        NavHost(
            navController = LocalNavController.current!!,
            startDestination = Route.Main.toString()
        ) {
            composable(route = Route.Main.toString()) {
                MainScreen(keepSplash)
            }
            composable(route = Route.List.toString()) {
                val visibleState = MutableTransitionState(false).apply {
                    targetState = true
                }
                ListScreen()
            }
            composable(
                route = Route.Song.toString() + "/{title}",
                arguments = listOf(
                    navArgument(name = "title") {
                        type = NavType.StringType
                        nullable = false
                    }
                )
            ) {
                it.arguments?.getString("title")
                    ?.let { title ->
                        SongScreen(
                            title = title
                        )

                    }
            }
            composable(route = Route.Gallery.toString()) {
                GalleryScreen()
            }
            composable(
                route = Route.Picture.toString() + "/{page}",
                arguments = listOf(
                    navArgument(name = "page") {
                        type = NavType.IntType
                        nullable = false
                    }
                )) {
                it.arguments?.getInt("page")?.let { page ->
                    val visibleState = MutableTransitionState(false).apply {
                        targetState = true
                    }
                        PictureScreen(
                            page = page
                        )
                }

            }
        }
    }

}