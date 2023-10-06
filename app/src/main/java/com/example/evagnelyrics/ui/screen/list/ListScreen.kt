package com.example.evagnelyrics.ui.screen.list

import android.media.MediaPlayer
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.evagnelyrics.R
import com.example.evagnelyrics.core.LocalNavController
import com.example.evagnelyrics.core.Resource
import com.example.evagnelyrics.core.dimen
import com.example.evagnelyrics.ui.navigation.Route
import com.example.evagnelyrics.ui.theme.component.DiscJockeyBehaviour
import com.example.evagnelyrics.ui.theme.component.EvKeyboardAction
import com.example.evagnelyrics.ui.theme.component.EvText
import com.example.evagnelyrics.ui.theme.component.EvTextField
import com.example.evagnelyrics.ui.theme.component.EvTextStyle
import com.example.evagnelyrics.ui.util.SlideFromLeft
import com.example.evagnelyrics.ui.util.SlideFromRight
import com.example.evagnelyrics.ui.util.SlideInOutFrom
import com.example.evagnelyrics.ui.util.Vertically
import com.example.evagnelyrics.ui.util.Where
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    viewModel: ListViewModel = koinViewModel(),
    navController: NavHostController = LocalNavController.current!!
) {
    //composable
    val snackBarState = SnackbarHostState()

    //viewModel states
    val favMode by viewModel.favState
    val searchMode by viewModel.searchMode.collectAsStateWithLifecycle()
    val text by viewModel.searchField.observeAsState()
    val titles by viewModel.titles.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    SlideFromRight(visible = !searchMode) {
                        EvText(
                            resource = R.string.songs,
                            style = EvTextStyle.Head,
                        )
                    }
                    SlideFromRight(visible = searchMode) {
                        EvTextField(
                            value = text.orEmpty(),
                            modifier = Modifier
                                .fillMaxWidth(),
                            style = EvTextStyle.Head,
                            hint = R.string.search_song_hint,
                            keyboardAction = EvKeyboardAction.Go,
                        ) { viewModel.searching(it) }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (searchMode) {
                            viewModel.toggleSearchMode()
                        } else {
                            navController.popBackStack()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = "back arrow",
                        )
                    }
                },
                actions = {
                    SlideFromRight(visible = !searchMode) {
                        Row {
                            IconButton(onClick = { viewModel.onFavMode() }) {
                                Icon(
                                    imageVector = Icons.Rounded.Favorite,
                                    contentDescription = "fav mode",
                                    tint = if (favMode) Color.Red else MaterialTheme.colorScheme.onPrimary
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            IconButton(onClick = {
                                viewModel.toggleSearchMode()
                            }) {
                                Icon(
                                    imageVector = Icons.Rounded.Search,
                                    contentDescription = "search",
                                )
                            }

                        }
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(snackBarState) }
    ) {
        SongsList(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            songs = titles,
        ) { title: String ->
            navController.navigate(Route.Song.toString() + "/$title")
        }


        //collect uiStates
        LaunchedEffect(key1 = true) {
            viewModel.uiState.collectLatest { state ->
                when (state) {
                    is Resource.Error -> {
                        snackBarState.showSnackbar(state.msg)
                    }

                    is Resource.Success -> {}
                }
            }
        }
    }
}

@Composable
fun SongsList(
    modifier: Modifier = Modifier,
    songs: List<String> = emptyList(),
    navTo: (title: String) -> Unit = {},
) {
    val mutableTransition = MutableTransitionState(false).apply {
        targetState = true
    }

    LazyColumn(modifier) {
        itemsIndexed(songs) { index, item ->
            SlideInOutFrom(
                where = Where.Vertical(Vertically.Top),
                visibleState = mutableTransition,
                delayMillis = 0,
                hasBounce = true,
                durationMillis = 50,
                dampingRatio = 1.1f - (index.toFloat() / (songs.size - 1))
            ) {
                SongItem(
                    title = item,
                ) {
                    navTo(it)
                    //something about add the screen to nav stack
                }
            }
        }
    }
}

enum class Audio { Pause, Running }

@Composable
fun SongItem(
    modifier: Modifier = Modifier,
    title: String,
    viewModel: ListViewModel = koinViewModel(),
    navTo: (title: String) -> Unit = {},
) {
    val context = LocalContext.current

    val mainAudio by viewModel.audioState.collectAsState()

    val mediaPlayer: MediaPlayer by remember {
        mutableStateOf(
            MediaPlayer.create(context, getResourceSong(title))
        )
    }

    val playingSong by viewModel.playingSong

    //To observe the lifecycle
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE) {
                viewModel.setAudioState(Audio.Pause)
                viewModel.onPlayer(PlayerAction.Pause)
            } else if (event == Lifecycle.Event.ON_DESTROY) {
                viewModel.onPlayer(PlayerAction.Clean)
                mediaPlayer.release()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top = MaterialTheme.dimen.verySmall,
                start = MaterialTheme.dimen.superSmall,
                end = MaterialTheme.dimen.superSmall
            )
            .height(MaterialTheme.dimen.almostGiant)
            .clip(shape = RoundedCornerShape(20))
            .background(color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(20))
            .clickable {
                navTo(title)
            }
    ) {
        SlideFromLeft(
            visible = mainAudio == Audio.Running && playingSong == title,
            mediaPlayer.duration
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        MaterialTheme.colorScheme.secondary,
                        shape = RoundedCornerShape(20)
                    )
            )
        }

        Row(
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(MaterialTheme.dimen.mediumSmall))
            DiscJockeyBehaviour(
                modifier = Modifier
                    .fillMaxHeight(),
                isPlaying = mainAudio == Audio.Running && playingSong == title,
                onClick = {
                    if (mainAudio == Audio.Pause) {

                        viewModel.onPlayer(
                            action = PlayerAction.Play,
                            song = getResourceSong(title),
                            onComplete = {
                                viewModel.setAudioState(Audio.Pause)
                            }
                        )
                        viewModel.setAudioState(Audio.Running, title = title)
                    } else if (mainAudio == Audio.Running) {
                        when (playingSong) {
                            title -> {
                                viewModel.onPlayer(PlayerAction.Pause)
                            }

                            else -> {
                                //there is another running
                                viewModel.onPlayer(PlayerAction.Pause)

                                viewModel.onPlayer(
                                    action = PlayerAction.Play,
                                    song = getResourceSong(title),
                                    onComplete = {
                                        viewModel.setAudioState(Audio.Pause)
                                    }
                                )
                                viewModel.setAudioState(Audio.Running, title = title)
                            }
                        }
                    }
                }
            ) {
                Image(
                    painter = painterResource(id = getCover(title) ?: R.drawable.img3_7228),
                    contentDescription = "cover image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(all = MaterialTheme.dimen.verySmall)
                        .aspectRatio(1f)
                        .clip(CircleShape)
                )
            }
            Spacer(modifier = Modifier.width(MaterialTheme.dimen.mediumSmall))
            Text(text = title)
        }

        //collect favs list
        val favTitles by viewModel.favoritesFlow.collectAsState(emptyList())
        FavIcon(
            title,
            favTitles.map { it.title },
            Modifier.align(Alignment.CenterEnd)
        ) { viewModel.favAction(it) }

    }
}

@Composable
fun FavIcon(
    title: String,
    favTitles: List<String> = emptyList(),
    modifier: Modifier,
    favAction: (String) -> Unit
) {
    IconButton(
        modifier = modifier.padding(end = MaterialTheme.dimen.mediumSmall),
        onClick = { favAction(title) }) {
        Icon(
            imageVector = Icons.Rounded.Favorite,
            contentDescription = "fav button",
            tint = if (favTitles.contains(title)) Color.Red else MaterialTheme.colorScheme.onPrimary
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
    SongsList(songs = listOf("Hello"))
}

fun getResourceSong(title: String) =
    when (title) {
        "Falsedad" -> R.raw.falsedad
        "Tu perdón" -> R.raw.tu_perdon
        "Tú" -> R.raw.tu
        else -> R.raw.stereo_love
    }

fun getCover(title: String) =
    when (title) {
        "Falsedad" -> R.drawable.cover_falsedad
        "Tu perdón" -> R.drawable.cover_tu_perdon
        "Tú" -> R.drawable.cover_tu
        else -> null
    }