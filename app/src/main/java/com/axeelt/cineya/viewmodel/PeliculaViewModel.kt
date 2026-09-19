package com.axeelt.cineya.viewmodel

import androidx.lifecycle.ViewModel
import com.axeelt.cineya.model.Pelicula
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PeliculaViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _peliculas =
        MutableStateFlow<List<Pelicula>>(emptyList())

    val peliculas = _peliculas.asStateFlow()

    private val _mensaje =
        MutableStateFlow("")

    val mensaje = _mensaje.asStateFlow()


    // CREATE
    fun agregarPelicula(
        pelicula: Pelicula,
        onSuccess: () -> Unit = {}
    ) {

        db.collection("peliculas")
            .add(pelicula)
            .addOnSuccessListener {

                _mensaje.value =
                    "Película registrada correctamente"

                obtenerPeliculas()

                onSuccess()
            }
            .addOnFailureListener { error ->

                _mensaje.value =
                    error.message
                        ?: "Error al registrar película"
            }
    }


    // READ
    fun obtenerPeliculas() {

        db.collection("peliculas")
            .get()
            .addOnSuccessListener { resultado ->

                val lista = resultado.map { documento ->

                    documento
                        .toObject(Pelicula::class.java)
                        .copy(id = documento.id)
                }

                _peliculas.value = lista
            }
            .addOnFailureListener { error ->

                _mensaje.value =
                    error.message
                        ?: "Error al obtener películas"
            }
    }


    // UPDATE
    fun actualizarPelicula(
        pelicula: Pelicula,
        onSuccess: () -> Unit = {}
    ) {

        db.collection("peliculas")
            .document(pelicula.id)
            .set(pelicula)
            .addOnSuccessListener {

                _mensaje.value =
                    "Película actualizada correctamente"

                obtenerPeliculas()

                onSuccess()
            }
            .addOnFailureListener { error ->

                _mensaje.value =
                    error.message
                        ?: "Error al actualizar película"
            }
    }


    // DELETE
    fun eliminarPelicula(id: String) {

        db.collection("peliculas")
            .document(id)
            .delete()
            .addOnSuccessListener {

                _mensaje.value =
                    "Película eliminada correctamente"

                obtenerPeliculas()
            }
            .addOnFailureListener { error ->

                _mensaje.value =
                    error.message
                        ?: "Error al eliminar película"
            }
    }
}