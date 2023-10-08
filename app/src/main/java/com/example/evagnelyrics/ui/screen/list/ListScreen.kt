package com.example.evagnelyrics.ui.screen.list

import android.media.MediaPlayer
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
import androidx.compose.material3.LocalContentColor
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.evagnelyrics.R
import com.example.evagnelyrics.core.LocalNavController
import com.example.evagnelyrics.core.Resource
import com.example.evagnelyrics.core.dimen
import com.example.evagnelyrics.domain.model.Lyric
import com.example.evagnelyrics.ui.navigation.Route
import com.example.evagnelyrics.ui.screen.list.model.ListFilter
import com.example.evagnelyrics.ui.screen.list.model.ListFilterEvent
import com.example.evagnelyrics.ui.screen.list.model.PlayerUi
import com.example.evagnelyrics.ui.theme.component.DiscJockeyBehaviour
import com.example.evagnelyrics.ui.util.SlideFromRight
import com.example.evagnelyrics.ui.util.SlideInOutFrom
import com.example.evagnelyrics.ui.util.Vertically
import com.example.evagnelyrics.ui.util.Where
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen() {
    val viewModel: ListViewModel = koinViewModel()
    val navController: NavHostController = LocalNavController.current!!
    //composable
    val snackBarState = remember { SnackbarHostState() }

    //viewModel states
    val filterState by viewModel.filterState.collectAsStateWithLifecycle()
    val lyrics by viewModel.allLyricsFlow.collectAsStateWithLifecycle(initialValue = emptyList())
    val favoriteLyrics by viewModel.favoritesFlow.collectAsStateWithLifecycle(initialValue = emptyList())
    val player by viewModel.playerState.collectAsStateWithLifecycle()

    val lyricsState by remember {
        derivedStateOf {
            val filtered = if (filterState.favorite) favoriteLyrics
            else lyrics

            filtered.filter { it.title.contains(filterState.searchText, true) }
        }
    }

    val onBack: () -> Unit = {
        if (filterState.searchMode) {
            viewModel.onFilterEvent(ListFilterEvent.SearchMode)
            viewModel.onFilterEvent(ListFilterEvent.SearchText(""))
        } else {
            navController.popBackStack()
        }
    }

    BackHandler(onBack = onBack)

    Scaffold(
        topBar = {
            ListTopAppBar(
                isPlaying = player.audio == Audio.Running,
                filterState = filterState,
                onFilterEvent = viewModel::onFilterEvent,
                onBack = onBack
            )
        },
        snackbarHost = { SnackbarHost(snackBarState) }
    ) {
        SongsList(
            modifier = Modifier
                .padding(it),
            songs = lyricsState,
            favoriteMode = filterState.favorite,
            playerUi = player,
            onPlayer = { data -> viewModel.onPlayer(data.action, data.rawSong, data.lyric) },
            onFavAction = viewModel::favAction
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
@OptIn(ExperimentalMaterial3Api::class)
private fun ListTopAppBar(
    isPlaying: Boolean,
    filterState: ListFilter,
    onFilterEvent: (ListFilterEvent) -> Unit,
    onBack: () -> Unit
) {
    TopAppBar(
        title = {
            SlideFromRight(visible = !filterState.searchMode) {
                Text(text = stringResource(id = R.string.songs))
            }
            SlideFromRight(visible = filterState.searchMode) {
                OutlinedTextField(
                    value = filterState.searchText,
                    onValueChange = { onFilterEvent(ListFilterEvent.SearchText(it)) },
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
            SlideFromRight(visible = !filterState.searchMode) {
                Row {
                    IconButton(
                        onClick = { onFilterEvent(ListFilterEvent.Favorite) },
                        enabled = !isPlaying,
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Favorite,
                            contentDescription = "fav mode",
                            tint = if (filterState.favorite) Color.Red else LocalContentColor.current
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    IconButton(
                        onClick = {
                            onFilterEvent(ListFilterEvent.SearchMode)
                        },
                        enabled = !isPlaying,
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Search,
                            contentDescription = "search",
                        )
                    }

                }
            }
        },
    )
}

@Composable
fun SongsList(
    modifier: Modifier = Modifier,
    songs: List<Lyric> = emptyList(),
    favoriteMode: Boolean,
    playerUi: PlayerUi,
    onPlayer: (PlayerActionData) -> Unit,
    onFavAction: (title: String) -> Unit,
    navTo: (title: String) -> Unit = {},
) {
    val mutableTransition = remember(favoriteMode){
        MutableTransitionState(false).apply {
            targetState = true
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small)
    ) {
        itemsIndexed(songs, key = {index, item ->item.title}) { index, item ->
            SlideInOutFrom(
                where = Where.Vertical(Vertically.Top),
                visibleState = mutableTransition,
                delayMillis = 0,
                hasBounce = true,
                durationMillis = 50,
                dampingRatio = 1.1f - (index.toFloat() / (songs.size - 1))
            ) {
                SongItem(
                    lyric = item,
                    favoriteMode = favoriteMode,
                    playerUi = playerUi,
                    onPlayer = onPlayer,
                    onFavAction = onFavAction,
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
    lyric: Lyric,
    modifier: Modifier = Modifier,
    favoriteMode: Boolean,
    playerUi: PlayerUi,
    onPlayer: (PlayerActionData) -> Unit,
    onFavAction: (title: String) -> Unit,
    navTo: (title: String) -> Unit = {},
) {
    val context = LocalContext.current

    val mediaPlayer: MediaPlayer by remember {
        mutableStateOf(
            MediaPlayer.create(context, getResourceSong(lyric.title))
        )
    }

    //To observe the lifecycle
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE) {
                onPlayer(PlayerActionData(PlayerAction.Pause))
            } else if (event == Lifecycle.Event.ON_DESTROY) {
                onPlayer(PlayerActionData(PlayerAction.Clean))
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
        onClick = { navTo(lyric.title) }
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
                    isPlaying = playerUi isPlaying lyric,
                    onClick = {
                        if (playerUi.isPlaying()) {

                            onPlayer(PlayerActionData(PlayerAction.Pause))

                            if (playerUi.playingSong != lyric) {
                                //new song running
                                onPlayer(
                                    PlayerActionData(
                                        action = PlayerAction.Play,
                                        rawSong = getResourceSong(lyric.title),
                                        lyric = lyric
                                    )
                                )
                            }
                        } else {

                            onPlayer(
                                PlayerActionData(
                                    action = PlayerAction.Play,
                                    rawSong = getResourceSong(lyric.title),
                                    lyric = lyric
                                )
                            )
                        }
                    }
                ) {
                    Image(
                        painter = painterResource(
                            id = getCover(lyric.title) ?: R.drawable.img3_7228
                        ),
                        contentDescription = "cover image",
                        contentScale = ContentScale.Crop,
                    )
                }
                Spacer(modifier = Modifier.width(MaterialTheme.dimen.mediumSmall))
                Text(text = lyric.title)
            }

            if (!favoriteMode) {//collect favs list
                IconButton(onClick = { onFavAction(lyric.title) }) {
                    Icon(
                        imageVector = Icons.Rounded.Favorite,
                        contentDescription = "favorite icon",
                        tint = if (lyric.favorite) Color.Red else LocalContentColor.current
                    )
                }
            }
        }

        val isPlaying by remember(playerUi) {
            derivedStateOf { playerUi isPlaying lyric }
        }

        if (isPlaying) {
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