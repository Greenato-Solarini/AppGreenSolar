package com.GreenatoSolarini.myapplicationjetpackcompose.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.GreenatoSolarini.myapplicationjetpackcompose.model.Producto
import com.GreenatoSolarini.myapplicationjetpackcompose.model.ProyectoSolar

@Database(
    entities = [
        Producto::class,
        ProyectoSolar::class
    ],
    version = 2,                 // 👈 si antes era 1, súbelo a 2
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productoDao(): ProductoDao

    abstract fun proyectoDao(): ProyectoDao
}
