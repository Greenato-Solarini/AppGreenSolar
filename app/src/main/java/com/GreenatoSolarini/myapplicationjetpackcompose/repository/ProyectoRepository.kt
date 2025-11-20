package com.GreenatoSolarini.myapplicationjetpackcompose.repository

import com.GreenatoSolarini.myapplicationjetpackcompose.data.local.ProyectoDao
import com.GreenatoSolarini.myapplicationjetpackcompose.model.ProyectoSolar
import kotlinx.coroutines.flow.Flow

class ProyectoRepository(
    private val dao: ProyectoDao
) {

    val proyectos: Flow<List<ProyectoSolar>> = dao.getAll()

    suspend fun crearProyecto(proyecto: ProyectoSolar) {
        dao.insert(proyecto)
    }

    suspend fun actualizarProyecto(proyecto: ProyectoSolar) {
        dao.update(proyecto)
    }

    suspend fun eliminarProyecto(proyecto: ProyectoSolar) {
        dao.delete(proyecto)
    }

    suspend fun obtenerPorId(id: Int): ProyectoSolar? {
        return dao.getById(id)
    }
}
