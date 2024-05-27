package com.example.amiibopgl.pantallas

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.amiibopgl.R
import com.example.amiibopgl.shared.LoginModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun InicioSesion(navController: NavController, viewModel: LoginModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Inicio sesión") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("Inicio") }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Ir atras"
                        )
                    }
                }
            )
        },
        content = {
            Surface(
                color = Color.White,
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    BackHandler {
                    }
                    Image(
                        painter = painterResource(id = R.drawable.amiibologo),
                        contentDescription = "Logo",
                        modifier = Modifier.size(200.dp)
                    )
                    Text(
                        text = "¡Estás a un paso de conocer todo sobre los Amiibo!",
                        color = Color.Black,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Correo electrónico",
                        color = Color.Black,
                        modifier = Modifier.align(Alignment.Start),
                        style = TextStyle(fontWeight = FontWeight.Bold)
                    )
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Ingrese su correo") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        keyboardActions = KeyboardActions(onNext = { keyboardController?.hide() }),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Contraseña",
                        color = Color.Black,
                        modifier = Modifier.align(Alignment.Start),
                        style = TextStyle(fontWeight = FontWeight.Bold)
                    )
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Ingrese su contraseña") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(25.dp).background(Color.Gray))
                    Button(
                        onClick = { viewModel.singIn(email, password) { navController.navigate("Principal") } },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(45.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(text = "Iniciar sesión", style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp))
                    }
                    Spacer(modifier = Modifier.height(16.dp).background(Color.Gray))
                    Spacer(modifier = Modifier.height(2.dp).fillMaxWidth().background(Color.LightGray))
                    Spacer(modifier = Modifier.height(16.dp).background(Color.Gray))
                    Text(text = "¿No tienes una cuenta?", color = Color.Gray, style = TextStyle(fontWeight = FontWeight.Bold))
                    Spacer(modifier = Modifier.height(16.dp).background(Color.Gray))
                    Button(
                        onClick = { navController.navigate("Registro") },
                        modifier = Modifier
                            .width(200.dp)
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Text(text = "Crear cuenta", style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp))
                    }
                }
            }
        }
    )


}