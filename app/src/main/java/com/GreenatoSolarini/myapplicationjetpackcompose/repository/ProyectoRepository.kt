package com.GreenatoSolarini.myapplicationjetpackcompose.repository

import com.GreenatoSolarini.myapplicationjetpackcompose.model.ProyectoSolar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProyectoRepository {

    // Lista de proyectos, parte vacía
    private val _proyectos = MutableStateFlow<List<ProyectoSolar>>(emptyList())
    val proyectos: StateFlow<List<ProyectoSolar>> = _proyectos

    private var ultimoId: Int = 0

    fun obtenerProyectoPorId(id: Int): ProyectoSolar? {
        return _proyectos.value.firstOrNull { it.id == id }
    }

    fun crearProyecto(
        nombre: String,
        cliente: String,
        direccion: String,
        estado: String
    ) {
        ultimoId += 1
        val nuevoProyecto = ProyectoSolar(
            id = ultimoId,
            nombre = nombre,
            cliente = cliente,
            direccion = direccion,
            estado = estado,
            avancePorcentaje = 0,
            produccionActualW = 0,
            consumoActualW = 0,
            ahorroHoyClp = 0
        )
        _proyectos.value = _proyectos.value + nuevoProyecto
    }

    fun actualizarProyecto(proyectoActualizado: ProyectoSolar) {
        _proyectos.value = _proyectos.value.map { proyecto ->
            if (proyecto.id == proyectoActualizado.id) proyectoActualizado else proyecto
        }
    }

    fun eliminarProyecto(id: Int) {
        _proyectos.value = _proyectos.value.filterNot { it.id == id }
    }
}
