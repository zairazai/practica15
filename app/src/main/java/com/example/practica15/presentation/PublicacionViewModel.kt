package com.example.practica15.presentation

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.practica15.data.AppDatabase
import com.example.practica15.data.Publicacion
import com.example.practica15.data.PublicacionRepository
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PublicacionViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = PublicacionRepository(
        AppDatabase.getDatabase(application).publicacionDao()
    )

    // Lista de publicaciones guardadas en Room
    val publicaciones = repository.obtenerPublicaciones()

    // Datos del formulario
    var titulo = mutableStateOf("")
    var descripcion = mutableStateOf("")
    var fotoUri = mutableStateOf<String?>(null)
    var fecha = mutableStateOf(System.currentTimeMillis())

    // Reinicia el formulario con la fecha actual
    fun nuevoFormulario() {
        titulo.value = ""
        descripcion.value = ""
        fotoUri.value = null
        fecha.value = System.currentTimeMillis()
    }

    // Guarda la foto tomada con la cámara en el almacenamiento de la app
    fun guardarFotoCamara(bitmap: Bitmap) {
        val archivo = crearArchivoFoto()
        archivo.outputStream().use { salida ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, salida)
        }
        fotoUri.value = Uri.fromFile(archivo).toString()
    }

    // Copia la imagen elegida de la galería al almacenamiento de la app
    // para poder verla aunque se cierre la aplicación
    fun guardarFotoGaleria(uri: Uri) {
        val archivo = crearArchivoFoto()
        val contentResolver = getApplication<Application>().contentResolver
        contentResolver.openInputStream(uri)?.use { entrada ->
            archivo.outputStream().use { salida ->
                entrada.copyTo(salida)
            }
        }
        fotoUri.value = Uri.fromFile(archivo).toString()
    }

    private fun crearArchivoFoto(): File {
        val carpeta = getApplication<Application>().filesDir
        return File(carpeta, "foto_${System.currentTimeMillis()}.jpg")
    }

    fun guardarPublicacion(onGuardado: () -> Unit) {
        val nueva = Publicacion(
            titulo = titulo.value.trim(),
            descripcion = descripcion.value.trim(),
            fotoUri = fotoUri.value,
            fecha = fecha.value
        )

        viewModelScope.launch {
            repository.guardar(nueva)
            onGuardado()
        }
    }
}

// Convierte la fecha en milisegundos a texto
fun formatearFecha(fecha: Long): String {
    val formato = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return formato.format(Date(fecha))
}
