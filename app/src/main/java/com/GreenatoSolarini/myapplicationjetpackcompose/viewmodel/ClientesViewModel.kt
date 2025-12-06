package com.GreenatoSolarini.myapplicationjetpackcompose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.GreenatoSolarini.myapplicationjetpackcompose.model.Cliente
import com.GreenatoSolarini.myapplicationjetpackcompose.repository.ClienteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ClientesViewModel(
    private val repository: ClienteRepository
) : ViewModel() {

    // Exposición del estado de los clientes como StateFlow
    val clientes: StateFlow<List<Cliente>> = repository.clientes.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // -------- VALIDACIONES (públicas para pruebas) --------
    fun esNombreValido(nombre: String): Boolean {
        return nombre.isNotBlank() && nombre.length >= 2
    }

    fun esEmailValido(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()
    }

    fun esTelefonoValido(telefono: String): Boolean {
        val digitos = telefono.filter { it.isDigit() }
        return digitos.length in 8..12
    }

    fun esDireccionValida(direccion: String): Boolean {
        return direccion.isNotBlank()
    }

    fun esComunaValida(comuna: String): Boolean {
        return comuna.isNotBlank()
    }

    // -------- CRUD CLIENTES --------

    /**
     * Intenta agregar un cliente tras validar los datos.
     * @return true si los datos son válidos y se inicia la inserción, false si falla la validación.
     */
    fun agregarCliente(
        nombre: String,
        email: String,
        telefono: String,
        direccion: String,
        comuna: String
    ): Boolean {
        if (!esNombreValido(nombre)) return false
        if (!esEmailValido(email)) return false
        if (!esTelefonoValido(telefono)) return false
        if (!esDireccionValida(direccion)) return false
        if (!esComunaValida(comuna)) return false

        viewModelScope.launch {
            repository.insertar(
                Cliente(
                    id = 0, // Room autogenera el ID
                    nombre = nombre,
                    email = email,
                    telefono = telefono,
                    direccion = direccion,
                    comuna = comuna
                )
            )
        }
        return true
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