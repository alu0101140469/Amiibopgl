package com.example.amiibopgl.retrofit

import com.example.amiibopgl.model.Raiz
import com.example.amiibopgl.model.RaizGS
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL =
    "https://www.amiiboapi.com/api/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

object AmiiboApi {
    val retrofitService: AmbiiboAPIInterface by lazy {
        retrofit.create(AmbiiboAPIInterface::class.java)
    }
}

interface AmbiiboAPIInterface {
    @GET("amiibo/")
    suspend fun getAmiibos(@Query("gameseries") gameSeries : String?): Response<Raiz>

    @GET("gameseries")
    suspend fun getGameSeries(): Response<RaizGS>
}


