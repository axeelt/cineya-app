package com.axeelt.cineya.screen.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.axeelt.cineya.model.Pelicula
import com.axeelt.cineya.ui.theme.CineYaCard
import com.axeelt.cineya.ui.theme.CineYaRed
import com.axeelt.cineya.ui.theme.CineYaText
import com.axeelt.cineya.ui.theme.CineYaTextSecondary
import com.axeelt.cineya.viewmodel.PeliculaViewModel

@Composable
fun AdminScreen(
    onNuevaPelicula: () -> Unit,
    onEditarPelicula: (Pelicula) -> Unit,
    onCerrarSesion: () -> Unit,
    viewModel: PeliculaViewModel = viewModel()
) {

    val peliculas by viewModel.peliculas.collectAsState()

    var peliculaAEliminar by remember {
        mutableStateOf<Pelicula?>(null)
    }

    LaunchedEffect(Unit) {
        viewModel.obtenerPeliculas()
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,

        floatingActionButton = {
            FloatingActionButton(
                onClick = onNuevaPelicula,
                containerColor = CineYaRed,
                contentColor = Color.White
            ) {
                Text(
                    text = "+",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(16.dp)
        ) {

            // ENCABEZADO
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column {

                    Text(
                        text = "CINEYA!",
                        color = CineYaRed,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Panel administrativo",
                        color = CineYaTextSecondary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                TextButton(
                    onClick = onCerrarSesion
                ) {
                    Text(
                        text = "Salir",
                        color = CineYaRed
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(28.dp)
            )

            Text(
                text = "Gestión de películas",
                color = CineYaText,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = "Administra las películas de CineYa!",
                color = CineYaTextSecondary
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            if (peliculas.isEmpty()) {

                Text(
                    text = "No hay películas registradas",
                    color = CineYaTextSecondary
                )

            } else {

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    items(
                        items = peliculas,
                        key = { pelicula ->
                            pelicula.id
                        }
                    ) { pelicula ->

                        PeliculaAdminItem(
                            pelicula = pelicula,

                            onEditar = {
                                onEditarPelicula(pelicula)
                            },

                            onEliminar = {
                                peliculaAEliminar = pelicula
                            }
                        )
                    }
                }
            }
        }
    }

    if (peliculaAEliminar != null) {

        AlertDialog(
            onDismissRequest = {
                peliculaAEliminar = null
            },

            title = {
                Text("Eliminar película")
            },

            text = {
                Text(
                    "¿Deseas eliminar ${peliculaAEliminar?.titulo}?"
                )
            },

            confirmButton = {

                TextButton(
                    onClick = {

                        peliculaAEliminar?.let { pelicula ->

                            viewModel.eliminarPelicula(
                                pelicula.id
                            )
                        }

                        peliculaAEliminar = null
                    }
                ) {

                    Text(
                        text = "Eliminar",
                        color = CineYaRed
                    )
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {
                        peliculaAEliminar = null
                    }
                ) {

                    Text("Cancelar")
                }
            }
        )
    }
}


@Composable
fun PeliculaAdminItem(
    pelicula: Pelicula,
    onEditar: () -> Unit,
    onEliminar: () -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = CineYaCard
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = pelicula.titulo,
                color = CineYaText,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = pelicula.genero,
                color = CineYaTextSecondary,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = pelicula.estado.uppercase(),
                color = if (
                    pelicula.estado == "En cartelera"
                ) {
                    Color(0xFF4CAF50)
                } else {
                    CineYaRed
                },
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                Button(
                    onClick = onEditar,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CineYaRed
                    )
                ) {

                    Text("Editar")
                }

                Button(
                    onClick = onEliminar,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF333333)
                    )
                ) {

                    Text("Eliminar")
                }
            }
        }
    }
}