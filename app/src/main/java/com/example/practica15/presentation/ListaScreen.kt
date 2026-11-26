package com.example.practica15.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.practica15.data.Publicacion
import com.example.practica15.ui.theme.Blanco
import com.example.practica15.ui.theme.GrisClaro
import com.example.practica15.ui.theme.Negro
import com.example.practica15.ui.theme.Rojo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaScreen(
    viewModel: PublicacionViewModel,
    onNuevaPublicacion: () -> Unit
) {

    // Se actualiza sola cada vez que Room guarda una publicación
    val publicaciones by viewModel.publicaciones.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "RedSocial",
                        fontWeight = FontWeight.ExtraBold,
                        color = Blanco
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Negro
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNuevaPublicacion,
                containerColor = Rojo,
                contentColor = Blanco
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nueva publicación")
            }
        },
        containerColor = GrisClaro
    ) { paddingValues ->

        if (publicaciones.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Aún no hay publicaciones. ¡Crea la primera!")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(publicaciones) { publicacion ->
                    PublicacionItem(publicacion)
                }
            }
        }
    }
}

@Composable
fun PublicacionItem(publicacion: Publicacion) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Blanco),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {

        // Encabezado con el título y la fecha
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Rojo, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = publicacion.titulo.take(1).uppercase(),
                    color = Blanco,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = publicacion.titulo,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Negro
                )
                Text(
                    text = formatearFecha(publicacion.fecha),
                    fontSize = 12.sp,
                    color = Rojo
                )
            }
        }

        // Foto de la publicación
        if (publicacion.fotoUri != null) {
            AsyncImage(
                model = publicacion.fotoUri,
                contentDescription = publicacion.titulo,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
            )
        }

        // Descripción
        Text(
            text = publicacion.descripcion,
            modifier = Modifier.padding(12.dp),
            color = Negro
        )
    }
}
