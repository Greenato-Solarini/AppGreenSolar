package com.GreenatoSolarini.myapplicationjetpackcompose.repository

import com.GreenatoSolarini.myapplicationjetpackcompose.model.ProyectoSolar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProyectoRepository {

    // Lista simulada de proyectos solares (mock)
    private val _proyectos = MutableStateFlow(
        listOf(
            ProyectoSolar(
                id = 1,
                nombre = "Casa Lota",
                cliente = "María Pérez",
                direccion = "Concepcion, Región del Biobío",
                estado = "En instalación",
                avancePorcentaje = 45,
                produccionActualW = 780,
                consumoActualW = 640,
                ahorroHoyClp = 910
            ),
            ProyectoSolar(
                id = 2,
                nombre = "Panadería Sol Naciente",
                cliente = "Panadería Sol Naciente",
                direccion = "Santiago, Región Metropolitana",
                estado = "Operativo",
                avancePorcentaje = 100,
                produccionActualW = 3200,
                consumoActualW = 2800,
                ahorroHoyClp = 5630
            )
        )
    )

    val proyectos: StateFlow<List<ProyectoSolar>> = _proyectos

    fun obtenerProyectoPorId(id: Int): ProyectoSolar? {
        return _proyectos.value.firstOrNull { it.id == id }
    }
}
