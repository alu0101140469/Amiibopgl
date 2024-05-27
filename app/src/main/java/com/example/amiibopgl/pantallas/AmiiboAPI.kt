package com.example.amiibopgl.pantallas

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.compose.ui.unit.sp
import com.example.amiibopgl.shared.estadoApi


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmiiboAPI(navController: NavController) {
    val viewModel: ViewModelRetrofit = viewModel()
    val estado by viewModel.estadoLlamada.collectAsState()
    val listaAmiibos by viewModel.listaAmiibos.collectAsState()
    val listaGameSeries by viewModel.listaGS.collectAsState()
    val busquedaTexto by viewModel.textoBusqueda.collectAsState()
    val activo by viewModel.activo.collectAsState()

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
                text = "Cargando amibos!",
                fontSize = 18.sp
            )
        }
    } else {
        Column() {
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
                placeholder = { Text("Selecciona una serie de amiibos!") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = { Icon(Icons.Default.MoreVert, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                content = {
                    Column(Modifier.verticalScroll(rememberScrollState())) {
                        listaGameSeries!!.amiibo?.distinctBy { it.name }!!
                            .let { amiibos ->
                                if (busquedaTexto.isNotEmpty() && busquedaTexto.isNotBlank()) {
                                    amiibos.filter {
                                        it.name!!.startsWith(
                                            busquedaTexto,
                                            true
                                        )
                                    }
                                } else amiibos
                            }.sortedBy { it.name!! }.forEach {
                                ListItem(
                                    headlineContent = { it.name?.let { it1 -> Text(it1) } },
                                    modifier = Modifier
                                        .clickable {
                                            viewModel.actualizarTextoBusqueda(it.name!!)
                                            viewModel.actualizarActivo(false)
                                            viewModel.searchAmiibos()
                                        }
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 4.dp)
                                )
                            }
                    }

                }
            )
            LazyColumn() {
                items(listaAmiibos!!.amiibo!!) {
                    ItemAmiibo(a = it)
                }
            }
        }
    }
}



@Composable
fun ItemAmiibo(a : Amiibo) {
    Card(elevation = CardDefaults.elevatedCardElevation(defaultElevation = 5.dp)) {
        Row(Modifier.fillMaxWidth()) {
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
                    text = it, modifier = Modifier.fillMaxWidth().align(Alignment.CenterVertically)
                        .wrapContentWidth(Alignment.CenterHorizontally, true),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}