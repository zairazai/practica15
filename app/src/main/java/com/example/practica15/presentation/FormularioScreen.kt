package com.example.practica15.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.example.practica15.ui.theme.Blanco
import com.example.practica15.ui.theme.GrisClaro
import com.example.practica15.ui.theme.Negro
import com.example.practica15.ui.theme.Rojo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioScreen(
    viewModel: PublicacionViewModel,
    onRegresar: () -> Unit
) {

    val context = LocalContext.current

    // Lanzador para abrir la cámara
    val camaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        if (bitmap != null) {
            viewModel.guardarFotoCamara(bitmap)
        }
    }

    // Lanzador para pedir el permiso de cámara
    val permisoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { concedido ->
        if (concedido) {
            camaraLauncher.launch(null)
        } else {
            Toast.makeText(
                context,
                "Permiso de cámara denegado",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // Lanzador para elegir una imagen de la galería
    // (el selector del sistema no necesita permiso de almacenamiento)
    val galeriaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.guardarFotoGaleria(uri)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nueva publicación", color = Blanco) },
                navigationIcon = {
                    IconButton(onClick = onRegresar) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar",
                            tint = Blanco
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Negro
                )
            )
        },
        containerColor = Blanco
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // Título
            OutlinedTextField(
                value = viewModel.titulo.value,
                onValueChange = { viewModel.titulo.value = it },
                label = { Text("Título") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // Descripción
            OutlinedTextField(
                value = viewModel.descripcion.value,
                onValueChange = { viewModel.descripcion.value = it },
                label = { Text("Descripción") },
                minLines = 3,
                modifier = Modifier.fillMaxWidth()
            )

            // Fecha generada automáticamente
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(GrisClaro, RoundedCornerShape(8.dp))
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.CalendarMonth,
                    contentDescription = "Fecha",
                    tint = Rojo
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Fecha: ${formatearFecha(viewModel.fecha.value)}",
                    fontWeight = FontWeight.Medium,
                    color = Negro
                )
            }

            // Vista previa de la foto
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(GrisClaro)
                    .border(2.dp, Negro, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (viewModel.fotoUri.value != null) {
                    AsyncImage(
                        model = viewModel.fotoUri.value,
                        contentDescription = "Foto seleccionada",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Text("Sin foto")
                }
            }

            // Botones de cámara y galería
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        if (ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.CAMERA
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            camaraLauncher.launch(null)
                        } else {
                            permisoLauncher.launch(Manifest.permission.CAMERA)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Negro),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.PhotoCamera, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Cámara")
                }

                Button(
                    onClick = { galeriaLauncher.launch("image/*") },
                    colors = ButtonDefaults.buttonColors(containerColor = Negro),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.PhotoLibrary, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Galería")
                }
            }

            // Guardar la publicación
            Button(
                onClick = {
                    viewModel.guardarPublicacion {
                        Toast.makeText(
                            context,
                            "Publicación guardada",
                            Toast.LENGTH_SHORT
                        ).show()
                        onRegresar()
                    }
                },
                enabled = viewModel.titulo.value.isNotBlank() &&
                        viewModel.descripcion.value.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = Rojo),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("PUBLICAR", fontWeight = FontWeight.Bold)
            }
        }
    }
}
