package com.GreenatoSolarini.myapplicationjetpackcompose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.GreenatoSolarini.myapplicationjetpackcompose.model.Instalador
import com.GreenatoSolarini.myapplicationjetpackcompose.repository.InstaladorRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class InstaladoresViewModel(
    private val repository: InstaladorRepository
) : ViewModel() {

    // Flujo con todos los instaladores
    val instaladores = repository.instaladores.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Agregar un instalador nuevo
    fun agregarInstalador(
        nombre: String,
        telefono: String,
        email: String
    ) {
        viewModelScope.launch {
            repository.insertar(
                Instalador(
                    nombre = nombre,
                    telefono = telefono,
                    email = email
                )
            )
        }
    }

    // Actualizar un instalador existente
    fun actualizarInstalador(instalador: Instalador) {
        viewModelScope.launch {
            repository.actualizar(instalador)
        }
    }

    // Eliminar instalador
    fun eliminarInstalador(instalador: Instalador) {
        viewModelScope.launch {
            repository.eliminar(instalador)
        }
    }

    // Obtener instalador según ID
    fun obtenerInstaladorPorId(id: Int): Instalador? {
        return instaladores.value.firstOrNull { it.id == id }
    }
}


// ---------------- FABRICA ---------------- //

class InstaladoresViewModelFactory(
    private val repository: InstaladorRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InstaladoresViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InstaladoresViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
