package com.example.evagnelyrics.ui.compose.screen.gallery

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.evagnelyrics.R
import com.example.evagnelyrics.core.Items
import com.example.evagnelyrics.core.dimen
import com.example.evagnelyrics.ui.fragment.ACTION_BACK
import com.example.evagnelyrics.ui.fragment.ACTION_NEXT
import com.example.evagnelyrics.ui.theme.EvagneLyricsTheme

@Composable
fun GalleryScreen(
    navTo: (route: String) -> Unit,
) {
    EvagneLyricsTheme {

        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.title_wallpapers)) },
                navigationIcon = {
                    IconButton(onClick = { navTo(ACTION_BACK) }) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = "back on song"
                        )
                    }
                }
            )
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
                                navTo("$ACTION_NEXT/$index")
                            }
                    )
                }
            }
        }

    }
}