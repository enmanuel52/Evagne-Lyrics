package com.example.evagnelyrics.ui.screen.song

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.evagnelyrics.R
import com.example.evagnelyrics.core.LocalNavController
import com.example.evagnelyrics.core.dimen
import com.example.evagnelyrics.ui.theme.component.DiscJockeyBehaviour
import org.koin.androidx.compose.koinViewModel

@Composable
fun SongScreen(
    viewModel: SongViewModel = koinViewModel(),
    title: String = "",
    navController: NavHostController = LocalNavController.current!!,
) {
    //observe and fetch the song
    viewModel.fetch(title)
    //here i should observe when i download the dependencies
    val song by viewModel.song.observeAsState()//= LyricsEntity("Hello", "is me")

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBar(
            title = { Text(text = song?.title.orEmpty(), style = MaterialTheme.typography.h1) },
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
        Text(
            text = song?.letter.orEmpty(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.dimen.medium)
                .verticalScroll(rememberScrollState()),
            color = if (MaterialTheme.colors.isLight) MaterialTheme.colors.primary
            else MaterialTheme.colors.onBackground
        )

    }

    Box(modifier = Modifier.fillMaxSize()){
        val (play, onPlay) = remember {
            mutableStateOf(false)
        }

        DiscJockeyBehaviour(
            isPlaying = play,
            onClick = { onPlay(!play) },
            modifier = Modifier.size(200.dp).align(Alignment.Center)
        ) {
            Image(
                painter = painterResource(id = R.drawable.avicii_cover),
                contentDescription = "music cover",
                contentScale = ContentScale.Crop
            )
        }
    }


}

@Composable
@Preview("Song screen", showSystemUi = true)
fun SongScreenPreview() {
    SongScreen(title = "Hello")
}