package com.example.amiibopgl.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.amiibopgl.model.Amiibo
import com.example.amiibopgl.model.AmiiboRoot
import com.example.amiibopgl.model.GameSeriesRoot
import com.example.amiibopgl.retrofit.AmiiboApi.retrofitService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class estadoApi {
    IDLE, LOADING, SUCCESS, ERROR
}

class ViewModelRetrofit : ViewModel() {

    private val _listaAmiibos: MutableStateFlow<AmiiboRoot?> = MutableStateFlow(null)
    var listaAmiibos = _listaAmiibos.asStateFlow()


    private val _listaGS: MutableStateFlow<GameSeriesRoot?> = MutableStateFlow(null)
    var listaGS = _listaGS.asStateFlow()

    private val _estadoLlamada: MutableStateFlow<estadoApi> = MutableStateFlow(estadoApi.IDLE)
    var estadoLlamada = _estadoLlamada.asStateFlow()

    private val _textoBusqueda: MutableStateFlow<String> = MutableStateFlow("")
    var textoBusqueda = _textoBusqueda.asStateFlow()

    fun actualizarTextoBusqueda(s: String) {
        _textoBusqueda.value = s
    }

    private val _activo = MutableStateFlow(false)
    var activo = _activo.asStateFlow()

    private val _listaEnPosesion: MutableStateFlow<List<Amiibo>> = MutableStateFlow(emptyList())
    val listaEnPosesion = _listaEnPosesion.asStateFlow()

    fun agregarAmiiboAPosesion(amiibo: Amiibo) {
        _listaEnPosesion.value = _listaEnPosesion.value + amiibo
    }

    fun actualizarActivo(b: Boolean) {
        _activo.value = b
    }

    init {
        getAmiibos()
        getGameSeries()
    }

    fun getAmiibos() {
        _estadoLlamada.value = estadoApi.LOADING
        viewModelScope.launch {
            var respuesta = retrofitService.getAmiibos(null)
            if (respuesta.isSuccessful) {
                _listaAmiibos.value = respuesta.body()
                _estadoLlamada.value = estadoApi.SUCCESS
                println(respuesta.body().toString())
            } else println("Ha habido algún error " + respuesta.errorBody())
        }
    }

    fun getGameSeries() {
        _estadoLlamada.value = estadoApi.LOADING
        viewModelScope.launch {
            var respuesta = retrofitService.getGameSeries()
            if (respuesta.isSuccessful) {
                _listaGS.value = respuesta.body()
            } else println("Ha habido algún error " + respuesta.errorBody())
        }
    }

    fun searchAmiibos() {
        _estadoLlamada.value = estadoApi.LOADING
        println("Buscando los amiibos de la serie ${_textoBusqueda.value}")
        viewModelScope.launch {
            var respuesta = retrofitService.getAmiibos(_textoBusqueda.value)
            if (respuesta.isSuccessful) {
                _listaAmiibos.value = null
                _listaAmiibos.value = respuesta.body()
                _estadoLlamada.value = estadoApi.SUCCESS
            } else println("Ha habido algún error " + respuesta.errorBody())
        }
    }
}