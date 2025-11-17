package com.GreenatoSolarini.myapplicationjetpackcompose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    fun eliminarProducto(producto: Producto) {
        viewModelScope.launch {
            repository.eliminar(producto)
        }
    }
}
