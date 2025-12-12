package com.greenatosolarini.myapplicationjetpackcompose.viewmodel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.greenatosolarini.myapplicationjetpackcompose.model.Producto
import com.greenatosolarini.myapplicationjetpackcompose.repository.ProductoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class ProductosViewModel(
    private val repository: ProductoRepository
) : ViewModel() {

    val productos = repository.productos.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // ------- utilidades para convertir Bitmap <-> ByteArray -------
    private fun bitmapToBytes(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
        return stream.toByteArray()
    }

    private fun bytesToBitmap(bytes: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    // -------- CRUD bÃ¡sico --------
    fun agregarProducto(nombre: String, precio: Int, descripcion: String) {
        viewModelScope.launch {
            repository.insertar(
                Producto(
                    nombre = nombre,
                    precio = precio,
                    descripcion = descripcion
                )
            )
        }
    }

    fun obtenerProductoPorId(id: Int): Producto? {
        return productos.value.firstOrNull { it.id == id }
    }

    fun actualizarProducto(producto: Producto) {
        viewModelScope.launch {
            repository.actualizar(producto)
        }
    }

    fun eliminarProducto(producto: Producto) {
        viewModelScope.launch {
            repository.eliminar(producto)
        }
    }

    // -------- PERSISTENCIA DE FOTO --------

    fun guardarFotoProducto(id: Int, bitmap: Bitmap) {
        viewModelScope.launch {
            val productoActual = repository.obtenerPorId(id) ?: return@launch
            val bytes = bitmapToBytes(bitmap)
            val actualizado = productoActual.copy(foto = bytes)
            repository.actualizar(actualizado)
        }
    }

    fun obtenerFotoProducto(id: Int): Bitmap? {
        val producto = productos.value.firstOrNull { it.id == id } ?: return null
        val bytes = producto.foto ?: return null
        return bytesToBitmap(bytes)
    }
}

