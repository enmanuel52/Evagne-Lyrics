package com.example.evagnelyrics.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.evagnelyrics.ui.screen.gallery.GalleryScreen
import com.example.evagnelyrics.ui.screen.picture.PictureScreen
import kotlinx.serialization.Serializable


@Serializable
data object GalleryGraphDestination

@Serializable
data object GalleryDestination

@Serializable
data class PictureDestination(
    val page: Int,
)

fun NavHostController.navigateToGalleryGraph(navOptions: NavOptions? = null) {
    navigate(GalleryGraphDestination, navOptions)
}

fun NavHostController.navigateToPicture(page: Int, navOptions: NavOptions? = null) {
    navigate(PictureDestination(page), navOptions)
}

fun NavGraphBuilder.galleryGraph(
    onBack: () -> Unit,
    onPicture: (page: Int) -> Unit
) {
    navigation<GalleryGraphDestination>(GalleryDestination) {
        composable<GalleryDestination> {
            GalleryScreen(onBack, onPicture)
        }

        composable<PictureDestination> {
            val destination = it.toRoute<PictureDestination>()
            PictureScreen(destination.page, onBack)
        }
    }
}