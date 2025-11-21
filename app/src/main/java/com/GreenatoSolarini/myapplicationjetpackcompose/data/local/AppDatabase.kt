package com.GreenatoSolarini.myapplicationjetpackcompose.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.GreenatoSolarini.myapplicationjetpackcompose.model.Producto
import com.GreenatoSolarini.myapplicationjetpackcompose.model.ProyectoSolar
import com.GreenatoSolarini.myapplicationjetpackcompose.model.Cliente

@Database(
    entities = [
        Producto::class,
        ProyectoSolar::class,
        Cliente::class
    ],
    version = 3,          // 👈 si antes era 2, ahora 3;
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productoDao(): ProductoDao
    abstract fun proyectoDao(): ProyectoDao
    abstract fun clienteDao(): ClienteDao   // 👈 NUEVO
}
