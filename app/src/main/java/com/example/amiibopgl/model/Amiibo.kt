package com.example.amiibopgl.model

class Amiibo {
    var image: String? = null
    var name: String? = null
    var amiiboSeries: String? = null
    var release: Release? = null
    var character: String? = null
    var gameSeries: String? = null
    var head: String? = null
    var tail: String? = null
    var type: String? = null
}

class Release {
    var eu: String? = null
    var au: String? = null
    var na: String? = null
    var jp: String? = null
}

class AmiiboRoot {
    var amiibo: ArrayList<Amiibo>? = null
}