package com.example.amiibopgl.nav

sealed class Rutas(val ruta: String){
    object Inicio : Rutas("Inicio")
    object Principal : Rutas("Principal")
    object InicioSesion : Rutas("InicioSesion")
    object PerfilUsuario : Rutas("PerfilUsuario")
    object Registro : Rutas("Registro")
    object AmiiboAPI : Rutas("AmiiboAPI")
    object PersonalizadaAPI : Rutas("PersonalizadaAPI")
}