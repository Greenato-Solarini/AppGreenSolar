package com.GreenatoSolarini.myapplicationjetpackcompose.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "instaladores")
data class Instalador(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val telefono: String = "",
    val email: String = ""
)

