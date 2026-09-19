package com.axeelt.cineya.viewmodel

import androidx.lifecycle.ViewModel
import com.axeelt.cineya.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel : ViewModel() {

    // Firebase Authentication
    private val auth = FirebaseAuth.getInstance()

    // Cloud Firestore
    private val db = FirebaseFirestore.getInstance()

    // Estado interno que solo puede modificar el ViewModel
    private val _uiState = MutableStateFlow<AuthState>(AuthState.Idle)

    // Estado que puede observar la interfaz
    val uiState = _uiState.asStateFlow()


    // RF01 - REGISTRO DE USUARIO
    fun registrar(
        nombre: String,
        correo: String,
        password: String
    ) {

        _uiState.value = AuthState.Cargando

        auth.createUserWithEmailAndPassword(
            correo,
            password
        )
            .addOnSuccessListener { resultado ->

                val uid = resultado.user?.uid

                if (uid != null) {

                    val usuario = Usuario(
                        nombre = nombre,
                        correo = correo,
                        rol = "usuario"
                    )

                    db.collection("usuarios")
                        .document(uid)
                        .set(usuario)
                        .addOnSuccessListener {

                            _uiState.value = AuthState.Exito
                        }
                        .addOnFailureListener { error ->

                            _uiState.value = AuthState.Error(
                                error.message
                                    ?: "Error al guardar usuario"
                            )
                        }

                } else {

                    _uiState.value = AuthState.Error(
                        "No se pudo obtener el identificador del usuario"
                    )
                }
            }
            .addOnFailureListener { error ->

                _uiState.value = AuthState.Error(
                    error.message
                        ?: "Error al registrar usuario"
                )
            }
    }


    // RF02 - INICIO DE SESIÓN
    fun iniciarSesion(
        correo: String,
        password: String
    ) {

        _uiState.value = AuthState.Cargando

        auth.signInWithEmailAndPassword(
            correo,
            password
        )
            .addOnSuccessListener { resultado ->

                val uid = resultado.user?.uid

                if (uid != null) {

                    db.collection("usuarios")
                        .document(uid)
                        .get()
                        .addOnSuccessListener { documento ->

                            if (documento.exists()) {

                                val rol =
                                    documento.getString("rol")
                                        ?: "usuario"

                                _uiState.value =
                                    AuthState.LoginExitoso(rol)

                            } else {

                                _uiState.value =
                                    AuthState.Error(
                                        "No se encontraron los datos del usuario"
                                    )
                            }
                        }
                        .addOnFailureListener { error ->

                            _uiState.value =
                                AuthState.Error(
                                    error.message
                                        ?: "Error al obtener los datos del usuario"
                                )
                        }

                } else {

                    _uiState.value =
                        AuthState.Error(
                            "No se pudo obtener el identificador del usuario"
                        )
                }
            }
            .addOnFailureListener { error ->

                _uiState.value =
                    AuthState.Error(
                        error.message
                            ?: "Correo o contraseña incorrectos"
                    )
            }
    }


    // CERRAR SESIÓN
    fun cerrarSesion() {

        auth.signOut()

        _uiState.value = AuthState.Idle
    }
}


// Posibles estados de Authentication
sealed class AuthState {

    // No está ocurriendo ninguna operación
    object Idle : AuthState()

    // Firebase está procesando una operación
    object Cargando : AuthState()

    // Registro realizado correctamente
    object Exito : AuthState()

    // Login realizado correctamente
    // También guarda el rol del usuario
    data class LoginExitoso(
        val rol: String
    ) : AuthState()

    // Ocurrió algún error
    data class Error(
        val mensaje: String
    ) : AuthState()
}