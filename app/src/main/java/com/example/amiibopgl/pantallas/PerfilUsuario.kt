package com.example.amiibopgl.pantallas

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.amiibopgl.R

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilUsuario(navController: NavController) {
    var correo by remember { mutableStateOf("usuario@example.com") }
    var contraseña by remember { mutableStateOf("********") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil del usuario") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("Principal") }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Ir atrás"
                        )
                    }
                }
            )
        },
        content = { paddingValues ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                color = Color.White
            ) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .align(Alignment.TopCenter),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(50.dp))
                        ProfileImage()
                        Spacer(modifier = Modifier.height(16.dp))
                        ProfileInfo(correo = correo, contraseña = contraseña)
                    }
                    Image(
                        painter = painterResource(id = R.drawable.grupoamiibos),
                        contentDescription = "Logo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.4f)
                            .align(Alignment.BottomCenter)
                    )
                }
            }
        }
    )
}

@Composable
fun ProfileImage() {
    Image(
        painter = painterResource(id = R.drawable.user),
        contentDescription = "Imagen de perfil",
        modifier = Modifier
            .size(120.dp)
            .clip(CircleShape),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun ProfileInfo(correo: String, contraseña: String) {
    Text(
        text = "Correo: $correo",
        fontSize = 18.sp,
        color = Color.Black
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = "Contraseña: $contraseña",
        fontSize = 18.sp,
        color = Color.Black
    )
}
