package com.example.amiibopgl.pantallas

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.amiibopgl.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Principal(navController: NavController) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current

    var selectedItem by remember { mutableStateOf("Principal") }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(navController, drawerState, scope, auth, context, selectedItem) {
                selectedItem = it
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
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .background(Color.White)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.splatoon),
                        contentDescription = "Logo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                            .align(Alignment.TopCenter)
                            .background(Brush.verticalGradient(colors = listOf(Color.Black, Color.Transparent)))
                    ) {
                        Text(
                            "La base de datos completa de los Amiibo de Nintendo.",
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(horizontal = 20.dp)
                        )
                    }
                    // Overlay text at the bottom
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                            .align(Alignment.BottomCenter)
                            .background(Brush.verticalGradient(colors = listOf(Color.Transparent, Color.Black)))
                    ) {
                        Text(
                            "Puedes comenzar accediendo a la API.",
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(horizontal = 20.dp)
                        )
                    }
                }
            }
        )
    }
}

@Composable
fun DrawerContent(
    navController: NavController,
    drawerState: DrawerState,
    scope: CoroutineScope,
    auth: FirebaseAuth,
    context: Context,
    selectedItem: String,
    onItemSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(top = 48.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DrawerHeader()
        DrawerItem(label = "Principal", selected = selectedItem == "Principal", onClick = {
            onItemSelected("Principal")
            scope.launch { drawerState.close() }
            navController.navigate("Principal")
        })
        DrawerItem(label = "Amiibo API", selected = selectedItem == "Amiibo API", onClick = {
            onItemSelected("Amiibo API")
            scope.launch { drawerState.close() }
            navController.navigate("AmiiboAPI")
        })
        DrawerItem(label = "Cerrar sesión", selected = false, onClick = {
            auth.signOut()
            navController.navigate("InicioSesion") { popUpTo("Principal") { inclusive = true } }
            scope.launch { drawerState.close() }
        })
        DrawerItem(label = "Salir", selected = false, onClick = {
            if (context is Activity) {
                context.finishAffinity()
            }
        })
    }
}

@Composable
fun DrawerHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Menú",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun DrawerItem(label: String, selected: Boolean, onClick: () -> Unit) {
    val backgroundColor = if (selected) Color(0xFF8B4513) else Color.Transparent // Marrón si está seleccionado
    val contentColor = if (selected) Color.White else MaterialTheme.colorScheme.onSurface

    Text(
        text = label,
        color = contentColor,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp)
            .background(color = backgroundColor, shape = RoundedCornerShape(8.dp)), // Fondo marrón y redondeado si está seleccionado
        style = MaterialTheme.typography.bodyLarge
    )
}
