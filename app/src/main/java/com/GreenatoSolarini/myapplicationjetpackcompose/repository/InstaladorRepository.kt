package com.GreenatoSolarini.myapplicationjetpackcompose.repository

import com.GreenatoSolarini.myapplicationjetpackcompose.data.local.InstaladorDao
import com.GreenatoSolarini.myapplicationjetpackcompose.model.Instalador
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
