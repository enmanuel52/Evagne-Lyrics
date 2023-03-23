package com.example.evagnelyrics.ui.screen.gallery

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.example.evagnelyrics.R
import com.example.evagnelyrics.core.Items
import com.example.evagnelyrics.core.LocalNavController
import com.example.evagnelyrics.core.dimen
import com.example.evagnelyrics.ui.navigation.Route
import com.example.evagnelyrics.ui.theme.component.EvText
import com.example.evagnelyrics.ui.theme.component.EvTextStyle
import com.example.evagnelyrics.ui.util.JustSlideFromRight

@Composable
fun GalleryScreen(
    navController: NavHostController = LocalNavController.current!!
) {
    val visibleState = MutableTransitionState(false).apply {
        targetState = true
    }

    JustSlideFromRight(
        visibleState = visibleState,
        durationMillis = 400,
        delayMillis = 100,
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = {
                    EvText(
                        resource = R.string.title_wallpapers,
                        style = EvTextStyle.Head,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = "back on song"
                        )
                    }
                }
            )
            PicturesList(navController)
        }
    }


}

@Composable
private fun PicturesList(navController: NavHostController) {
    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxWidth(),
        columns = GridCells.Fixed(count = 2),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small),
        contentPadding = PaddingValues(all = MaterialTheme.dimen.verySmall)
    ) {
        items(Items.images.size) { index ->
            Image(
                painter = painterResource(
                    id = when (index) {
                        0 -> R.drawable.img1_7185
                        1 -> R.drawable.img2_7190
                        2 -> R.drawable.img3_7228
                        3 -> R.drawable.img4_7236
                        4 -> R.drawable.img5_7281
                        else -> R.drawable.img1_7185
                    }
                ),
                contentDescription = "image # $index",
                modifier = Modifier
                    .clip(RoundedCornerShape(5))
                    .clickable {
                        navController.navigate(Route.Picture.toString() + "/$index")
                    }
            )
        }
    }
}