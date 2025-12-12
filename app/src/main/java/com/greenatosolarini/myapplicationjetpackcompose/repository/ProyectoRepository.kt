package com.greenatosolarini.myapplicationjetpackcompose.repository

import com.greenatosolarini.myapplicationjetpackcompose.data.local.ProyectoDao
import com.greenatosolarini.myapplicationjetpackcompose.model.ProyectoSolar
import kotlinx.coroutines.flow.Flow

class ProyectoRepository(
    private val dao: ProyectoDao
) {

    val proyectos: Flow<List<ProyectoSolar>> = dao.getAll()

    suspend fun insertar(proyecto: ProyectoSolar) {
        dao.insert(proyecto)
    }

    suspend fun actualizar(proyecto: ProyectoSolar) {
        dao.update(proyecto)
    }

    suspend fun eliminarPorId(id: Int) {
        dao.deleteById(id)
    }

    suspend fun obtenerPorId(id: Int): ProyectoSolar? {
        return dao.getById(id)
    }
}

