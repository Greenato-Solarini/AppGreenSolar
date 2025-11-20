package com.GreenatoSolarini.myapplicationjetpackcompose.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.GreenatoSolarini.myapplicationjetpackcompose.model.ProyectoSolar
import com.GreenatoSolarini.myapplicationjetpackcompose.repository.ProyectoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProyectosViewModel(
    private val repository: ProyectoRepository
) : ViewModel() {

    // Lista de proyectos desde Room (Flow -> StateFlow)
    val proyectos = repository.proyectos.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Mapa idProyecto -> foto (solo en memoria)
    private val _fotosProyecto = MutableStateFlow<Map<Int, Bitmap?>>(emptyMap())
    val fotosProyecto = _fotosProyecto.asStateFlow()

    fun guardarFotoProyecto(id: Int, bitmap: Bitmap) {
        val actual = _fotosProyecto.value.toMutableMap()
        actual[id] = bitmap
        _fotosProyecto.value = actual
    }

    fun obtenerFotoProyecto(id: Int): Bitmap? {
        return _fotosProyecto.value[id]
    }

    fun crearProyecto(
        nombre: String,
        cliente: String,
        direccion: String,
        estado: String
    ) {
        viewModelScope.launch {
            val nuevo = ProyectoSolar(
                nombre = nombre,
                cliente = cliente,
                direccion = direccion,
                estado = estado,
                // valores de ejemplo para monitoreo
                produccionActualW = 3500,
                consumoActualW = 2200,
                ahorroHoyClp = 4500
            )
            repository.crearProyecto(nuevo)
        }
    }

    fun actualizarProyecto(proyecto: ProyectoSolar) {
        viewModelScope.launch {
            repository.actualizarProyecto(proyecto)
        }
    }

    fun eliminarProyecto(proyecto: ProyectoSolar) {
        viewModelScope.launch {
            repository.eliminarProyecto(proyecto)
        }
    }

    // 👇 Esta es la que llama MainActivity
    fun eliminarProyectoPorId(id: Int) {
        val proyecto = proyectos.value.firstOrNull { it.id == id }
        if (proyecto != null) {
            eliminarProyecto(proyecto)
        }
    }

    fun obtenerProyectoPorId(id: Int): ProyectoSolar? {
        return proyectos.value.firstOrNull { it.id == id }
    }
}
