package com.axeelt.cineya.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.axeelt.cineya.model.Pelicula
import com.axeelt.cineya.screen.admin.AdminScreen
import com.axeelt.cineya.screen.admin.PeliculaFormScreen
import com.axeelt.cineya.screen.auth.LoginScreen
import com.axeelt.cineya.screen.auth.RegisterScreen
import com.axeelt.cineya.screen.user.CarteleraScreen
import com.axeelt.cineya.viewmodel.AuthViewModel
import com.axeelt.cineya.viewmodel.PeliculaViewModel

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    // ViewModel compartido para las películas
    val peliculaViewModel: PeliculaViewModel = viewModel()

    // Lo utilizaremos para cerrar la sesión
    val authViewModel: AuthViewModel = viewModel()

    // Guarda temporalmente la película que se desea editar.
    // Si es null, significa que se creará una película nueva.
    var peliculaEditando by remember {
        mutableStateOf<Pelicula?>(null)
    }

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {

        // LOGIN
        composable("login") {

            LoginScreen(

                onLoginExitoso = { rol ->

                    if (rol == "admin") {

                        navController.navigate("admin") {

                            popUpTo("login") {
                                inclusive = true
                            }
                        }

                    } else {

                        navController.navigate("cartelera") {

                            popUpTo("login") {
                                inclusive = true
                            }
                        }
                    }
                },

                onRegisterClick = {

                    navController.navigate("registro")
                }
            )
        }


        // REGISTRO
        composable("registro") {

            RegisterScreen(

                onRegistroExitoso = {

                    navController.popBackStack()
                },

                onLoginClick = {

                    navController.popBackStack()
                }
            )
        }


        // CARTELERA DEL USUARIO
        composable("cartelera") {

            CarteleraScreen(

                viewModel = peliculaViewModel,

                onCerrarSesion = {

                    // Cierra la sesión en Firebase
                    authViewModel.cerrarSesion()

                    // Regresa al Login
                    navController.navigate("login") {

                        popUpTo("cartelera") {
                            inclusive = true
                        }
                    }
                }
            )
        }


        // PANEL DEL ADMINISTRADOR
        composable("admin") {

            AdminScreen(

                viewModel = peliculaViewModel,

                onNuevaPelicula = {

                    peliculaEditando = null

                    navController.navigate(
                        "form_pelicula"
                    )
                },

                onEditarPelicula = { pelicula ->

                    peliculaEditando = pelicula

                    navController.navigate(
                        "form_pelicula"
                    )
                },

                onCerrarSesion = {

                    // Cierra la sesión en Firebase
                    authViewModel.cerrarSesion()

                    // Regresa al Login
                    navController.navigate("login") {

                        popUpTo("admin") {
                            inclusive = true
                        }
                    }
                }
            )
        }


        // CREAR / EDITAR PELÍCULA
        composable("form_pelicula") {

            PeliculaFormScreen(

                pelicula = peliculaEditando,

                viewModel = peliculaViewModel,

                onGuardarExitoso = {

                    navController.popBackStack()
                },

                onVolver = {

                    navController.popBackStack()
                }
            )
        }
    }
}