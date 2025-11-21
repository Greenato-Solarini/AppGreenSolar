package com.GreenatoSolarini.myapplicationjetpackcompose.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "proyectos")
data class ProyectoSolar(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val clienteId: Int,   // 👉 ahora guardamos el ID del cliente
    val direccion: String,
    val estado: String,
    val produccionActualW: Int,
    val consumoActualW: Int,
    val ahorroHoyClp: Int,
    val foto: ByteArray? = null
)
