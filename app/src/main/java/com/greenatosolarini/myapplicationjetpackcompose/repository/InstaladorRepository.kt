package com.greenatosolarini.myapplicationjetpackcompose.repository

import com.greenatosolarini.myapplicationjetpackcompose.data.local.InstaladorDao
import com.greenatosolarini.myapplicationjetpackcompose.model.Instalador
import kotlinx.coroutines.flow.Flow

class InstaladorRepository(
    private val dao: InstaladorDao
) {

    val instaladores: Flow<List<Instalador>> = dao.getAll()

    suspend fun insertar(instalador: Instalador) {
        dao.insert(instalador)
    }

    suspend fun actualizar(instalador: Instalador) {
        dao.update(instalador)
    }

    suspend fun eliminar(instalador: Instalador) {
        dao.delete(instalador)
    }

    suspend fun obtenerPorId(id: Int): Instalador? = dao.getById(id)
}

