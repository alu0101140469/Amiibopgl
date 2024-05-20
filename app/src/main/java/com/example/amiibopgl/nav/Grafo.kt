package com.example.amiibopgl.nav

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.amiibopgl.pantallas.AmiiboAPI
import com.example.amiibopgl.pantallas.Inicio
import com.example.amiibopgl.pantallas.InicioSesion
import com.example.amiibopgl.pantallas.PerfilUsuario
import com.example.amiibopgl.pantallas.PersonalizadaAPI
import com.example.amiibopgl.pantallas.Principal
import com.example.amiibopgl.pantallas.Registro

@Composable
fun GrafoNavegacion(){

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Rutas.Inicio.ruta){
        composable(Rutas.Inicio.ruta){
            Inicio(navController = navController)
        }
        composable(Rutas.Principal.ruta){
            Principal(navController = navController)
        }
        composable(Rutas.InicioSesion.ruta){
            InicioSesion(navController = navController)
        }
        composable(Rutas.PerfilUsuario.ruta){
            PerfilUsuario(navController = navController)
        }
        composable(Rutas.Registro.ruta){
            Registro(navController = navController)
        }
        composable(Rutas.AmiiboAPI.ruta){
            AmiiboAPI(navController = navController)
        }
        composable(Rutas.PersonalizadaAPI.ruta){
            PersonalizadaAPI(navController = navController)
        }
    }

}