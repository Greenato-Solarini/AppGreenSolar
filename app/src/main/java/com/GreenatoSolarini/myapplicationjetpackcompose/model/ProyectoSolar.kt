package com.GreenatoSolarini.myapplicationjetpackcompose.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "proyectos")
data class ProyectoSolar(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val cliente: String,
    val direccion: String,
    val estado: String,
    val produccionActualW: Int = 0,
    val consumoActualW: Int = 0,
    val ahorroHoyClp: Int = 0
)
