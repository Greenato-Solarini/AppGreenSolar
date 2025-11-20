package com.GreenatoSolarini.myapplicationjetpackcompose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.graphics.Bitmap
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.GreenatoSolarini.myapplicationjetpackcompose.model.Producto
import com.GreenatoSolarini.myapplicationjetpackcompose.repository.ProductoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProductosViewModel(
    private val repository: ProductoRepository
) : ViewModel() {

    val productos = repository.productos.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

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
        // Tomamos el producto desde el StateFlow actual
        return productos.value.firstOrNull { it.id == id }
    }

    fun actualizarProducto(producto: Producto) {
        viewModelScope.launch {
            repository.actualizarProducto(producto)
        }
    }

    fun eliminarProducto(producto: Producto) {
        viewModelScope.launch {
            repository.eliminar(producto)
        }
    }

    // Mapa idProducto -> foto (solo en memoria, no persistente)
    private val _fotosProducto = MutableStateFlow<Map<Int, Bitmap?>>(emptyMap())
    val fotosProducto = _fotosProducto.asStateFlow()

    fun guardarFotoProducto(id: Int, bitmap: Bitmap) {
        val actual = _fotosProducto.value.toMutableMap()
        actual[id] = bitmap
        _fotosProducto.value = actual
    }

    fun obtenerFotoProducto(id: Int): Bitmap? {
        return _fotosProducto.value[id]
    }

}
