package com.example.evagnelyrics.ui.screen.list

import android.media.MediaPlayer
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.evagnelyrics.EvagneLyricsApp.Companion.TAG
import com.example.evagnelyrics.R
import com.example.evagnelyrics.core.LocalNavController
import com.example.evagnelyrics.core.Resource
import com.example.evagnelyrics.core.dimen
import com.example.evagnelyrics.ui.navigation.Route
import com.example.evagnelyrics.ui.theme.component.DiscJockeyBehaviour
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
    val text by viewModel.searchState.collectAsStateWithLifecycle()
    val titles by viewModel.titles.collectAsState()

    val onBack: () -> Unit = {
        if (searchMode) {
            viewModel.toggleSearchMode()
        } else {
            navController.popBackStack()
        }
    }

    BackHandler(onBack = onBack)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    SlideFromRight(visible = !searchMode) {
                        Text(text = stringResource(id = R.string.songs))
                    }
                    SlideFromRight(visible = searchMode) {
                        OutlinedTextField(
                            value = text,
                            onValueChange = { viewModel.searching(it) },
                            label = { Text(text = stringResource(id = R.string.search_song_hint)) },
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = MaterialTheme.typography.bodyLarge
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
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
                                    tint = if (favMode) Color.Red else MaterialTheme.colorScheme.onBackground
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

    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small)
    ) {
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
                }
            }
        }
    }
}

enum class Audio { Pause, Running }

@OptIn(ExperimentalMaterial3Api::class)
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

    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = MaterialTheme.dimen.verySmall,
            )
            .animateContentSize(),
        onClick = { navTo(title) }
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(MaterialTheme.dimen.mediumSmall))
                DiscJockeyBehaviour(
                    modifier = Modifier
                        .padding(all = MaterialTheme.dimen.verySmall)
                        .height(MaterialTheme.dimen.almostGiant),
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
                    )
                }
                Spacer(modifier = Modifier.width(MaterialTheme.dimen.mediumSmall))
                Text(text = title)
            }

            //collect favs list
            val favTitles by viewModel.favoritesFlow.collectAsState(emptyList())
            FavIcon(
                title,
                favTitles = favTitles.map { it.title },
            ) { viewModel.favAction(it) }
        }

        val isPlaying by remember(mainAudio, playingSong) {
            derivedStateOf { mainAudio == Audio.Running && playingSong == title }
        }

        if (isPlaying) {
            LaunchedEffect(key1 = Unit, block = { Log.d(TAG, "SongItem: isRunning") })
            LinearAnimatedProgress(mediaPlayer.duration)
        }
    }
}

@Composable
private fun LinearAnimatedProgress(millis: Int) {
    var progress by remember {
        mutableFloatStateOf(0f)
    }

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        label = "song progress",
        animationSpec = tween(millis, easing = LinearEasing)
    )

    LinearProgressIndicator(
        progress = animatedProgress,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = MaterialTheme.dimen.verySmall,
                horizontal = MaterialTheme.dimen.small
            )
            .height(MaterialTheme.dimen.small)
            .clip(CircleShape)

    )

    LaunchedEffect(key1 = Unit, block = { progress = 1f })
}

@Composable
fun FavIcon(
    title: String,
    modifier: Modifier = Modifier,
    favTitles: List<String> = emptyList(),
    favAction: (String) -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = { favAction(title) }) {
        Icon(
            imageVector = Icons.Rounded.Favorite,
            contentDescription = "fav button",
            tint = if (favTitles.contains(title)) Color.Red else MaterialTheme.colorScheme.onBackground
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