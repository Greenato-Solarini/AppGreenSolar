package com.GreenatoSolarini.myapplicationjetpackcompose.repository

import com.GreenatoSolarini.myapplicationjetpackcompose.data.local.ProductoDao
import com.GreenatoSolarini.myapplicationjetpackcompose.model.Producto
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

    suspend fun obtenerPorId(id: Int): Producto? {
        return dao.getById(id)
    }

    suspend fun actualizarProducto(producto: Producto) {
        dao.update(producto)
    }
}
