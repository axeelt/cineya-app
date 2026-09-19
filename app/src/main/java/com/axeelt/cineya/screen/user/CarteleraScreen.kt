package com.axeelt.cineya.screen.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.axeelt.cineya.model.Pelicula
import com.axeelt.cineya.ui.theme.CineYaCard
import com.axeelt.cineya.ui.theme.CineYaRed
import com.axeelt.cineya.ui.theme.CineYaText
import com.axeelt.cineya.ui.theme.CineYaTextSecondary
import com.axeelt.cineya.viewmodel.PeliculaViewModel

@Composable
fun CarteleraScreen(
    onCerrarSesion: () -> Unit,
    viewModel: PeliculaViewModel = viewModel()
) {

    val peliculas by viewModel.peliculas.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.obtenerPeliculas()
    }

    val peliculasEnCartelera = peliculas.filter {
        it.estado == "En cartelera"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {

        // ENCABEZADO
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "CINEYA!",
                color = CineYaRed,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

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
            modifier = Modifier.height(20.dp)
        )

        Text(
            text = "En cartelera",
            color = CineYaText,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        if (peliculasEnCartelera.isEmpty()) {

            Text(
                text = "No hay películas disponibles en cartelera",
                color = CineYaTextSecondary
            )

        } else {

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {

                items(
                    items = peliculasEnCartelera,
                    key = { pelicula ->
                        pelicula.id
                    }
                ) { pelicula ->

                    PeliculaCarteleraItem(
                        pelicula = pelicula
                    )
                }
            }
        }
    }
}


@Composable
fun PeliculaCarteleraItem(
    pelicula: Pelicula
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = CineYaCard
        )
    ) {

        Column {

            // AFICHE
            if (pelicula.imagenUrl.isNotBlank()) {

                AsyncImage(
                    model = pelicula.imagenUrl,
                    contentDescription = pelicula.titulo,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(0.68f)
                )

            } else {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(0.68f)
                        .background(CineYaCard),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = "Sin afiche",
                        color = CineYaTextSecondary
                    )
                }
            }

            // INFORMACIÓN
            Column(
                modifier = Modifier.padding(12.dp)
            ) {

                // Reservamos el mismo espacio para 2 líneas de título
                Text(
                    text = pelicula.titulo,
                    color = CineYaText,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.height(48.dp)
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                // El género siempre ocupa máximo una línea
                Text(
                    text = pelicula.genero,
                    color = CineYaTextSecondary,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = "${pelicula.duracion} • ${pelicula.clasificacion}",
                    color = CineYaTextSecondary,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1
                )
            }
        }
    }
}