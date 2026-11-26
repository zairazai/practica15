package com.example.practica15.data

class PublicacionRepository(private val dao: PublicacionDao) {

    fun obtenerPublicaciones() = dao.obtenerPublicaciones()

    suspend fun guardar(publicacion: Publicacion) =
        dao.insertar(publicacion)
}
