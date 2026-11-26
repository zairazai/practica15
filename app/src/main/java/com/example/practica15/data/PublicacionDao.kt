package com.example.practica15.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PublicacionDao {

    @Insert
    suspend fun insertar(publicacion: Publicacion)

    // Las publicaciones más recientes aparecen primero
    @Query("SELECT * FROM publicaciones ORDER BY fecha DESC")
    fun obtenerPublicaciones(): Flow<List<Publicacion>>
}
