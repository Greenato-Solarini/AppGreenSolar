package com.GreenatoSolarini.myapplicationjetpackcompose.data.local

import com.GreenatoSolarini.myapplicationjetpackcompose.model.Cliente
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class FakeClienteDao : ClienteDao {
    override fun getAll(): Flow<List<Cliente>> = emptyFlow()
    override suspend fun insert(cliente: Cliente) = Unit
    override suspend fun update(cliente: Cliente) = Unit
    override suspend fun delete(cliente: Cliente) = Unit
    override suspend fun getById(id: Int): Cliente? = null
}