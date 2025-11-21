package com.GreenatoSolarini.myapplicationjetpackcompose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.GreenatoSolarini.myapplicationjetpackcompose.model.Cliente
import com.GreenatoSolarini.myapplicationjetpackcompose.repository.ClienteRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ClientesViewModel(
    private val repository: ClienteRepository
) : ViewModel() {

    // Flow<List<Cliente>> desde Room → expuesto como StateFlow
    val clientes = repository.clientes.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // -------- CRUD CLIENTES --------

    fun agregarCliente(
        nombre: String,
        email: String,
        telefono: String,
        direccion: String,
        comuna: String
    ) {
        viewModelScope.launch {
            repository.insertar(
                Cliente(
                    id = 0, // Room autogenera
                    nombre = nombre,
                    email = email,
                    telefono = telefono,
                    direccion = direccion,
                    comuna = comuna
                )
            )
        }
    }

    fun actualizarCliente(cliente: Cliente) {
        viewModelScope.launch {
            repository.actualizar(cliente)
        }
    }

    fun eliminarCliente(cliente: Cliente) {
        viewModelScope.launch {
            repository.eliminar(cliente)
        }
    }

    fun obtenerClientePorId(id: Int): Cliente? {
        return clientes.value.firstOrNull { it.id == id }
    }
}
