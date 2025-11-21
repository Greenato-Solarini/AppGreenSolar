package com.GreenatoSolarini.myapplicationjetpackcompose.repository

import com.GreenatoSolarini.myapplicationjetpackcompose.data.local.ClienteDao
import com.GreenatoSolarini.myapplicationjetpackcompose.model.Cliente
import kotlinx.coroutines.flow.Flow

class ClienteRepository(
    private val dao: ClienteDao
) {

    val clientes: Flow<List<Cliente>> = dao.getAll()

    suspend fun insertar(cliente: Cliente) {
        dao.insert(cliente)
    }

    suspend fun actualizar(cliente: Cliente) {
        dao.update(cliente)
    }

    suspend fun eliminar(cliente: Cliente) {
        dao.delete(cliente)
    }

    suspend fun obtenerPorId(id: Int): Cliente? {
        return dao.getById(id)
    }
}
