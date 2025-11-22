package com.GreenatoSolarini.myapplicationjetpackcompose.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.GreenatoSolarini.myapplicationjetpackcompose.model.Producto
import com.GreenatoSolarini.myapplicationjetpackcompose.model.ProyectoSolar
import com.GreenatoSolarini.myapplicationjetpackcompose.model.Cliente
import com.GreenatoSolarini.myapplicationjetpackcompose.model.Instalador

@Database(
    entities = [
        Producto::class,
        ProyectoSolar::class,
        Cliente::class,
        Instalador::class
    ],
    version = 6,                   // SUBE 1 LA VERSIÓN EN CADA CAMBIO DE APPDB(antes 5)
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productoDao(): ProductoDao
    abstract fun proyectoDao(): ProyectoDao
    abstract fun clienteDao(): ClienteDao
    abstract fun instaladorDao(): InstaladorDao
}
