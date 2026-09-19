package com.axeelt.cineya.model

data class Pelicula(
    val id: String = "",
    val titulo: String = "",
    val sinopsis: String = "",
    val genero: String = "",
    val duracion: String = "",
    val clasificacion: String = "",
    val fechaEstreno: String = "",
    val estado: String = "En cartelera",
    val imagenUrl: String = ""
)