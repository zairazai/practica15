package com.example.practica15.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "publicaciones")
data class Publicacion(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val titulo: String,

    val descripcion: String,

    // Ruta de la imagen guardada en el almacenamiento de la app
    val fotoUri: String?,

    // Fecha en milisegundos
    val fecha: Long
)
