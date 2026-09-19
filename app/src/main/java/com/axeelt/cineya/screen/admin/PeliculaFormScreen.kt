package com.axeelt.cineya.screen.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.axeelt.cineya.model.Pelicula
import com.axeelt.cineya.viewmodel.PeliculaViewModel

@Composable
fun PeliculaFormScreen(
    pelicula: Pelicula? = null,
    onGuardarExitoso: () -> Unit,
    onVolver: () -> Unit,
    viewModel: PeliculaViewModel = viewModel()
) {

    val esEdicion =
        pelicula != null && pelicula.id.isNotBlank()

    var titulo by remember(pelicula?.id) {
        mutableStateOf(pelicula?.titulo ?: "")
    }

    var sinopsis by remember(pelicula?.id) {
        mutableStateOf(pelicula?.sinopsis ?: "")
    }

    var genero by remember(pelicula?.id) {
        mutableStateOf(pelicula?.genero ?: "")
    }

    var duracion by remember(pelicula?.id) {
        mutableStateOf(pelicula?.duracion ?: "")
    }

    var clasificacion by remember(pelicula?.id) {
        mutableStateOf(pelicula?.clasificacion ?: "")
    }

    var fechaEstreno by remember(pelicula?.id) {
        mutableStateOf(pelicula?.fechaEstreno ?: "")
    }

    var estado by remember(pelicula?.id) {
        mutableStateOf(
            pelicula?.estado ?: "En cartelera"
        )
    }

    var imagenUrl by remember(pelicula?.id) {
        mutableStateOf(pelicula?.imagenUrl ?: "")
    }

    var mensajeValidacion by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(
                rememberScrollState()
            )
            .padding(24.dp)
    ) {

        Text(
            text = if (esEdicion) {
                "Editar película"
            } else {
                "Nueva película"
            },
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        OutlinedTextField(
            value = titulo,
            onValueChange = {
                titulo = it
            },
            label = {
                Text("Título")
            },
            colors = camposCineYa(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        OutlinedTextField(
            value = sinopsis,
            onValueChange = {
                sinopsis = it
            },
            label = {
                Text("Sinopsis")
            },
            colors = camposCineYa(),
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        OutlinedTextField(
            value = genero,
            onValueChange = {
                genero = it
            },
            label = {
                Text("Género")
            },
            colors = camposCineYa(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        OutlinedTextField(
            value = duracion,
            onValueChange = {
                duracion = it
            },
            label = {
                Text("Duración")
            },
            placeholder = {
                Text("Ejemplo: 120 min")
            },
            colors = camposCineYa(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        OutlinedTextField(
            value = clasificacion,
            onValueChange = {
                clasificacion = it
            },
            label = {
                Text("Clasificación")
            },
            placeholder = {
                Text("Ejemplo: +14")
            },
            colors = camposCineYa(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        OutlinedTextField(
            value = fechaEstreno,
            onValueChange = {
                fechaEstreno = it
            },
            label = {
                Text("Fecha de estreno")
            },
            placeholder = {
                Text("DD/MM/AAAA")
            },
            colors = camposCineYa(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Text(
            text = "Estado",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                RadioButton(
                    selected = estado == "En cartelera",
                    onClick = {
                        estado = "En cartelera"
                    }
                )

                Text(
                    text = "En cartelera",
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(
                modifier = Modifier.width(12.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                RadioButton(
                    selected = estado == "Próximamente",
                    onClick = {
                        estado = "Próximamente"
                    }
                )

                Text(
                    text = "Próximamente",
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        OutlinedTextField(
            value = imagenUrl,
            onValueChange = {
                imagenUrl = it
            },
            label = {
                Text("URL del afiche")
            },
            colors = camposCineYa(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Button(
            onClick = {

                mensajeValidacion = ""

                if (
                    titulo.isBlank() ||
                    sinopsis.isBlank() ||
                    genero.isBlank() ||
                    duracion.isBlank() ||
                    clasificacion.isBlank() ||
                    fechaEstreno.isBlank()
                ) {

                    mensajeValidacion =
                        "Completa los campos obligatorios"

                } else {

                    val peliculaGuardada = Pelicula(
                        id = pelicula?.id ?: "",
                        titulo = titulo,
                        sinopsis = sinopsis,
                        genero = genero,
                        duracion = duracion,
                        clasificacion = clasificacion,
                        fechaEstreno = fechaEstreno,
                        estado = estado,
                        imagenUrl = imagenUrl
                    )

                    if (esEdicion) {

                        viewModel.actualizarPelicula(
                            pelicula = peliculaGuardada,
                            onSuccess = onGuardarExitoso
                        )

                    } else {

                        viewModel.agregarPelicula(
                            pelicula = peliculaGuardada,
                            onSuccess = onGuardarExitoso
                        )
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                if (esEdicion) {
                    "Guardar cambios"
                } else {
                    "Registrar película"
                }
            )
        }

        TextButton(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = "Cancelar",
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

        Spacer(
            modifier = Modifier.height(24.dp)
        )
    }
}


@Composable
private fun camposCineYa() =
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
            MaterialTheme.colorScheme.primary,

        focusedPlaceholderColor =
            MaterialTheme.colorScheme.onSurface.copy(
                alpha = 0.5f
            ),

        unfocusedPlaceholderColor =
            MaterialTheme.colorScheme.onSurface.copy(
                alpha = 0.5f
            )
    )