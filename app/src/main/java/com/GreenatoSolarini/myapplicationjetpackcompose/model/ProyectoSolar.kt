package com.GreenatoSolarini.myapplicationjetpackcompose.model

data class ProyectoSolar(
    val id: Int,
    val nombre: String,
    val cliente: String,
    val direccion: String,
    val estado: String,          // "En instalación", "Operativo", etc.
    val avancePorcentaje: Int,   // 0 a 100
    val produccionActualW: Int,  // W generados ahora (simulado)
    val consumoActualW: Int,     // W consumidos ahora (simulado)
    val ahorroHoyClp: Int        // ahorro diario estimado en CLP
)
