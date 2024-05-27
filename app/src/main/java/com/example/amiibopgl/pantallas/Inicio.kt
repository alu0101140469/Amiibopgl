package com.example.amiibopgl.pantallas

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.RawResourceDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import com.example.amiibopgl.R

@OptIn(ExperimentalMaterial3Api::class)
@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun Inicio(navController: NavController) {
    val context = LocalContext.current
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            playWhenReady = true
            repeatMode = ExoPlayer.REPEAT_MODE_ALL
            volume = 0.1f
        }
    }

    val uri = RawResourceDataSource.buildRawResourceUri(R.raw.amiibovideo)
    val mediaItem = MediaItem.fromUri(uri)
    exoPlayer.setMediaItem(mediaItem)
    exoPlayer.prepare()

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.stop()
            exoPlayer.release()
        }
    }

    BackHandler {
        exoPlayer.stop()
        onBackPressedDispatcher?.onBackPressed()
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Image(
                        painter = painterResource(id = R.drawable.amiibologohorizontal),
                        contentDescription = "Logo",
                        modifier = Modifier.size(100.dp)
                    )
                },
                actions = {
                    IconButton(onClick = {
                        exoPlayer.stop()
                        navController.navigate("InicioSesion")
                    }) {
                        Icon(
                            imageVector = Icons.Filled.AccountCircle,
                            contentDescription = "Perfil de usuario"
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
        content = { paddingValues ->
            Box(modifier = Modifier.fillMaxSize()) {
                AndroidView(factory = {
                    PlayerView(it).apply {
                        useController = false
                        player = exoPlayer
                        resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                    }
                })
                Button(
                    onClick = {
                        exoPlayer.stop()
                        navController.navigate("Registro")
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF8B4513),
                        contentColor = Color.White
                    ),
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(text = "Comenzar", fontSize = 18.sp)
                }
            }
        }
    )
}