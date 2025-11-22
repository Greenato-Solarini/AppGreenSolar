package com.GreenatoSolarini.myapplicationjetpackcompose.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "proyectos")
data class ProyectoSolar(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    val nombre: String,
    val clienteId: Int,

    val direccion: String,
    val comuna: String,

    // 👇 Instalador debe estar AQUÍ (antes de estado)
    val instaladorId: Int? = null,

    val estado: String,

    val produccionActualW: Int,
    val consumoActualW: Int,
    val ahorroHoyClp: Int,

    val foto: ByteArray? = null
)
