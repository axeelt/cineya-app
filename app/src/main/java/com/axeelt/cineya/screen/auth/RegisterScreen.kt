package com.axeelt.cineya.screen.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.axeelt.cineya.viewmodel.AuthState
import com.axeelt.cineya.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel = viewModel(),
    onRegistroExitoso: () -> Unit,
    onLoginClick: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()

    var nombre by remember {
        mutableStateOf("")
    }

    var correo by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var confirmarPassword by remember {
        mutableStateOf("")
    }

    var mensajeValidacion by remember {
        mutableStateOf("")
    }

    LaunchedEffect(uiState) {
        if (uiState is AuthState.Exito) {
            onRegistroExitoso()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "CINEYA!",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "Crear cuenta",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        OutlinedTextField(
            value = nombre,
            onValueChange = {
                nombre = it
            },
            label = {
                Text("Nombre")
            },
            colors = camposAuthCineYa(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        OutlinedTextField(
            value = correo,
            onValueChange = {
                correo = it
            },
            label = {
                Text("Correo electrónico")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            colors = camposAuthCineYa(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
            },
            label = {
                Text("Contraseña")
            },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            colors = camposAuthCineYa(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        OutlinedTextField(
            value = confirmarPassword,
            onValueChange = {
                confirmarPassword = it
            },
            label = {
                Text("Confirmar contraseña")
            },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            colors = camposAuthCineYa(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        if (uiState is AuthState.Cargando) {

            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary
            )

        } else {

            Button(
                onClick = {

                    mensajeValidacion = ""

                    when {

                        nombre.isBlank() ||
                                correo.isBlank() ||
                                password.isBlank() ||
                                confirmarPassword.isBlank() -> {

                            mensajeValidacion =
                                "Completa todos los campos"
                        }

                        password.length < 6 -> {

                            mensajeValidacion =
                                "La contraseña debe tener al menos 6 caracteres"
                        }

                        password != confirmarPassword -> {

                            mensajeValidacion =
                                "Las contraseñas no coinciden"
                        }

                        else -> {

                            viewModel.registrar(
                                nombre = nombre,
                                correo = correo,
                                password = password
                            )
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {

                Text("Registrarme")
            }
        }

        TextButton(
            onClick = onLoginClick,
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = "¿Ya tienes una cuenta? Inicia sesión",
                color = MaterialTheme.colorScheme.primary
            )
        }

        if (mensajeValidacion.isNotEmpty()) {

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = mensajeValidacion,
                color = MaterialTheme.colorScheme.error
            )
        }

        if (uiState is AuthState.Error) {

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = (uiState as AuthState.Error).mensaje,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}


@Composable
private fun camposAuthCineYa() =
    OutlinedTextFieldDefaults.colors(

        focusedTextColor =
            MaterialTheme.colorScheme.onBackground,

        unfocusedTextColor =
            MaterialTheme.colorScheme.onBackground,

        focusedBorderColor =
            MaterialTheme.colorScheme.primary,

        unfocusedBorderColor =
            MaterialTheme.colorScheme.onSurface.copy(
                alpha = 0.5f
            ),

        focusedLabelColor =
            MaterialTheme.colorScheme.primary,

        unfocusedLabelColor =
            MaterialTheme.colorScheme.onSurface.copy(
                alpha = 0.7f
            ),

        cursorColor =
            MaterialTheme.colorScheme.primary
    )
