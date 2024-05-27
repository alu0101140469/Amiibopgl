package com.example.amiibopgl.pantallas

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.amiibopgl.model.Amiibo
import com.example.amiibopgl.shared.ViewModelRetrofit
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import com.example.amiibopgl.R
import com.example.amiibopgl.shared.estadoApi
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmiiboAPI(navController: NavController) {
    val viewModel: ViewModelRetrofit = viewModel()
    val estado by viewModel.estadoLlamada.collectAsState()
    val listaAmiibos by viewModel.listaAmiibos.collectAsState()
    val listaGameSeries by viewModel.listaGS.collectAsState()
    val busquedaTexto by viewModel.textoBusqueda.collectAsState()
    val activo by viewModel.activo.collectAsState()
    val listaEnPosesion by viewModel.listaEnPosesion.collectAsState()
    var tabIndex by remember { mutableStateOf(0) }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Box(modifier = Modifier.width(250.dp)) {
                DrawerContent(
                    navController,
                    drawerState,
                    scope,
                    FirebaseAuth.getInstance(),
                    LocalContext.current,
                    "Amiibo API"
                ) {}
            }
        }
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
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
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { if (drawerState.isClosed) drawerState.open() else drawerState.close() }
                        }) {
                            Icon(imageVector = Icons.Filled.Menu, contentDescription = "Menu")
                        }
                    },
                    actions = {
                        IconButton(onClick = { navController.navigate("PerfilUsuario") }) {
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
                Column(modifier = Modifier.padding(paddingValues)) {
                    Image(
                        painter = painterResource(id = R.drawable.grupoamiibos2),
                        contentDescription = "Logo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                    SearchBar(
                        query = busquedaTexto,
                        onQueryChange = {
                            viewModel.actualizarTextoBusqueda(it)
                        },
                        onSearch = {
                            viewModel.actualizarActivo(false)
                            viewModel.searchAmiibos()
                        },
                        active = activo,
                        onActiveChange = {
                            viewModel.actualizarActivo(it)
                        },
                        placeholder = { Text("Barra de búsqueda", color = Color.Black) },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = null,
                                tint = Color.Black
                            )
                        },
                        trailingIcon = {
                            Icon(
                                Icons.Default.MoreVert,
                                contentDescription = null,
                                tint = Color.Black
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Black),
                        colors = SearchBarDefaults.colors(
                            containerColor = Color.LightGray
                        ),
                        content = {
                            Column(Modifier
                                .verticalScroll(rememberScrollState())
                            ) {
                                listaGameSeries?.amiibo?.distinctBy { it.name }
                                    ?.let { amiibos ->
                                        val filteredAmiibos =
                                            if (busquedaTexto.isNotEmpty() && busquedaTexto.isNotBlank()) {
                                                amiibos.filter {
                                                    it.name!!.startsWith(
                                                        busquedaTexto,
                                                        true
                                                    )
                                                }
                                            } else amiibos

                                        filteredAmiibos.sortedBy { it.name!! }
                                            .forEach { gameSeries ->
                                                ListItem(
                                                    headlineContent = {
                                                        Text(
                                                            gameSeries.name ?: "",
                                                            color = Color.Black
                                                        )
                                                    },
                                                    modifier = Modifier
                                                        .clickable {
                                                            viewModel.actualizarTextoBusqueda(
                                                                gameSeries.name!!
                                                            )
                                                            viewModel.actualizarActivo(false)
                                                            viewModel.searchAmiibos()
                                                        }
                                                        .fillMaxWidth()
                                                        .padding(
                                                            horizontal = 16.dp,
                                                            vertical = 4.dp
                                                        )
                                                )
                                            }
                                    }
                            }
                        }
                    )

                    TabRow(selectedTabIndex = tabIndex, containerColor = Color.Black) {
                        Tab(selected = tabIndex == 0, onClick = { tabIndex = 0 }) {
                            Text(
                                "Lista amiibos",
                                modifier = Modifier.padding(16.dp),
                                color = Color.White
                            )
                        }
                        Tab(selected = tabIndex == 1, onClick = { tabIndex = 1 }) {
                            Text(
                                "En posesión",
                                modifier = Modifier.padding(16.dp),
                                color = Color.White
                            )
                        }
                    }

                    when (tabIndex) {
                        0 -> {
                            if (estado == estadoApi.LOADING) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    CircularProgressIndicator(modifier = Modifier.padding(bottom = 16.dp))
                                    Text(
                                        text = "Cargando...",
                                        fontSize = 18.sp
                                    )
                                }
                            } else {
                                LazyColumn {
                                    items(listaAmiibos?.amiibo ?: emptyList()) { amiibo ->
                                        AmiiboItem(
                                            a = amiibo,
                                            onLongClick = { viewModel.agregarAmiiboAPosesion(amiibo) }
                                        )
                                    }
                                }
                            }
                        }

                        1 -> {
                            LazyColumn {
                                items(listaEnPosesion) { amiibo ->
                                    AmiiboItem(a = amiibo, onLongClick = {})
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun AmiiboItem(a: Amiibo, onLongClick: () -> Unit) {
    Card(
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 5.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = { onLongClick() }
                    )
                }
        ) {
            var imageSize by remember { mutableStateOf(Size.Zero) }
            AsyncImage(
                model = a.image, contentDescription = null, modifier =
                Modifier
                    .size(200.dp)
                    .wrapContentWidth(
                        if (imageSize.width < 400) Alignment.CenterHorizontally else
                            Alignment.Start
                    )
                    .onSizeChanged { imageSize = it.toSize() }, contentScale = ContentScale.Fit,
                alignment = Alignment.Center
            )
            println("Image size: ${imageSize.width} x ${imageSize.height}")
            a.name?.let {
                Text(
                    text = it,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterVertically)
                        .wrapContentWidth(Alignment.CenterHorizontally, true),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
