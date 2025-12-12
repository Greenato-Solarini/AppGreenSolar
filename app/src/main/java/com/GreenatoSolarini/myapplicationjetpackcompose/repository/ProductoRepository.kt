package com.greenatosolarini.myapplicationjetpackcompose.repository

import com.greenatosolarini.myapplicationjetpackcompose.data.local.ProductoDao
import com.greenatosolarini.myapplicationjetpackcompose.model.Producto
import kotlinx.coroutines.flow.Flow

class ProductoRepository(
    private val dao: ProductoDao
) {

    val productos: Flow<List<Producto>> = dao.getAll()

    suspend fun insertar(producto: Producto) {
        dao.insert(producto)
    }

    suspend fun eliminar(producto: Producto) {
        dao.delete(producto)
    }

    suspend fun actualizar(producto: Producto) {
        dao.update(producto)
    }

    suspend fun obtenerPorId(id: Int): Producto? {
        return dao.getById(id)
    }
}

