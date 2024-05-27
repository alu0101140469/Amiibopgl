package com.example.amiibopgl.pantallas

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.amiibopgl.R
import com.example.amiibopgl.model.Amiibo
import com.example.amiibopgl.shared.ViewModelRetrofit
import com.example.amiibopgl.shared.estadoApi
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmiiboAPI(navController: NavController) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val viewModel: ViewModelRetrofit = viewModel()

    var tabIndex by remember { mutableStateOf(0) }
    val tabTitles = listOf("Todos los Amiibos", "Mi Lista")
    var searchText by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    var myAmiiboList by remember { mutableStateOf(listOf<Amiibo>()) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(navController, drawerState, scope, FirebaseAuth.getInstance(), LocalContext.current, tabIndex) {
                tabIndex = it
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                Column {
                    CenterAlignedTopAppBar(
                        title = { Text("Amiibo API") },
                        navigationIcon = {
                            IconButton(onClick = {
                                scope.launch { if (drawerState.isClosed) drawerState.open() else drawerState.close() }
                            }) {
                                Icon(Icons.Filled.Menu, contentDescription = "Menu")
                            }
                        },
                        actions = {
                            IconButton(onClick = { navController.navigate("PerfilUsuario") }) {
                                Icon(Icons.Filled.AccountCircle, contentDescription = "Perfil de usuario")
                            }
                        }
                    )
                    TabRow(selectedTabIndex = tabIndex) {
                        tabTitles.forEachIndexed { index, title ->
                            Tab(selected = tabIndex == index, onClick = { tabIndex = index }) {
                                Text(text = title, modifier = Modifier.padding(16.dp))
                            }
                        }
                    }
                    OutlinedTextField(
                        value = searchText,
                        onValueChange = { searchText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        placeholder = { Text("Buscar Amiibos") },
                        singleLine = true,
                        trailingIcon = {
                            IconButton(onClick = {
                                viewModel.buscarAmiibos(searchText)
                                keyboardController?.hide()
                            }) {
                                Icon(Icons.Default.Search, contentDescription = "Buscar")
                            }
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = {
                            viewModel.buscarAmiibos(searchText)
                            keyboardController?.hide()
                        })
                    )
                }
            },
            content = { padding ->
                Image(
                    painter = painterResource(id = R.drawable.grupoamiibos2),
                    contentDescription = "Logo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth()
                )
                Column(modifier = Modifier.padding(padding)) {
                    when (tabIndex) {
                        0 -> AllAmiibosTab(viewModel)
                        1 -> MyListTab(myAmiiboList)
                    }
                }
            }
        )
    }
}

@Composable
fun AllAmiibosTab(viewModel: ViewModelRetrofit) {
    val amiibos by viewModel.listaAmiibos.collectAsState()

    LazyColumn {
        items(amiibos) { amiibo ->
            AmiiboCard(amiibo = amiibo) {
                viewModel.addToMyList(amiibo)  // Assuming you have such functionality
            }
        }
    }
}

@Composable
fun MyListTab(amiiboList: List<Amiibo>) {
    LazyColumn {
        items(amiiboList) { amiibo ->
            AmiiboCard(amiibo = amiibo, onLongClick = {}) // No action on long click in my list
        }
    }
}

@Composable
fun AmiiboCard(amiibo: Amiibo, onLongClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable(onLongClick = onLongClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = amiibo.image,
                contentDescription = amiibo.name,
                modifier = Modifier.size(60.dp)
            )
            Text(amiibo.name ?: "Unknown", modifier = Modifier.padding(8.dp))
        }
    }
}
