package com.GreenatoSolarini.myapplicationjetpackcompose.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.GreenatoSolarini.myapplicationjetpackcompose.model.Producto

@Database(
    entities = [Producto::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productoDao(): ProductoDao
}
