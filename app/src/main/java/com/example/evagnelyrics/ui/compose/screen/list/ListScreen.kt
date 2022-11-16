package com.example.evagnelyrics.ui.compose.screen.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.PlaylistPlay
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.evagnelyrics.core.Resource
import com.example.evagnelyrics.ui.viewmodel.ListFragmentViewModel

@Composable
fun ListScreen(
    viewModel: ListFragmentViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    navForward: (title: String) -> Unit = {},
) {

    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text(text = "Canciones") },
                navigationIcon = {
                    IconButton(onClick = { /*go back*/ }) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = "back arrow"
                        )
                    }
                },
                actions = {
                    Row {
                        IconButton(onClick = { viewModel.onFavMode() }) {
                            Icon(
                                imageVector = Icons.Rounded.Favorite,
                                contentDescription = "fav mode"
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        IconButton(onClick = { /*do search*/ }) {
                            Icon(
                                imageVector = Icons.Rounded.Search,
                                contentDescription = "search"
                            )
                        }
                    }
                }
            )
        }
    ) {
        val titles by viewModel.titles.collectAsState(emptyList())
        SongsList(Modifier.padding(it), titles, navForward)

        //collect uiStates
        val uiState by viewModel.uiState.collectAsState(Resource.Success(Unit))

        LaunchedEffect(key1 = true) {
            when (val state = uiState) {
                is Resource.Error -> {
                    scaffoldState.snackbarHostState.showSnackbar(state.msg)
                }
                is Resource.Success -> {}
            }
        }
    }
}

@Composable
fun SongsList(
    modifier: Modifier = Modifier,
    songs: List<String> = emptyList(),
    navForward: (title: String) -> Unit = {},
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(songs.size) { index ->
            val visibleState = MutableTransitionState(false).apply {
                targetState = true
            }
            AnimatedVisibility(visibleState = visibleState,
                enter = slideInHorizontally(tween(delayMillis = index * 150)) { -it } + fadeIn(
                    tween(
                        delayMillis = index * 150
                    )
                )
            ) {
                SongItem(
                    modifier.clickable {
                        navForward(songs[index])
                    },
                    title = songs[index]
                )
            }
        }
    }
}

@Composable
fun SongItem(
    modifier: Modifier = Modifier,
    title: String,
    viewModel: ListFragmentViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    Row(
        modifier = modifier
            .padding(top = 6.dp)
            .border(width = 1.dp, shape = RoundedCornerShape(20), color = Color.Yellow)
            .background(Color.Magenta),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            Icon(imageVector = Icons.Rounded.PlaylistPlay, contentDescription = null)
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = title)
        }
        Spacer(modifier = Modifier.width(12.dp))
        //collect favs list
        val favsTitles by viewModel.favsTitles.collectAsState(emptyList())
        FavIcon(title, favsTitles)
    }
}

@Composable
fun FavIcon(title: String, favs: List<String> = emptyList()) {
    IconButton(
        modifier = Modifier.padding(end = 12.dp),
        onClick = { /*toggle fav*/ }) {
        Icon(
            imageVector = Icons.Rounded.Favorite,
            contentDescription = "fav button",
            tint = if (favs.contains(title)) Color.Red else Color.White
        )
    }
}

@Composable
@Preview(showSystemUi = true)
fun ListScreenPreview() {
    ListScreen()
}

@Composable
@Preview(showSystemUi = true)
fun SongsListScreenPreview() {
    SongsList(songs = listOf("Hola"))
}