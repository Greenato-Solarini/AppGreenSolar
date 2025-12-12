package com.greenatosolarini.myapplicationjetpackcompose.data.local

import android.content.Context
import androidx.room.Room
import androidx.room.Database
import androidx.room.RoomDatabase
import com.greenatosolarini.myapplicationjetpackcompose.model.Producto
import com.greenatosolarini.myapplicationjetpackcompose.model.ProyectoSolar
import com.greenatosolarini.myapplicationjetpackcompose.model.Cliente
import com.greenatosolarini.myapplicationjetpackcompose.model.Instalador

@Database(
    entities = [
        Producto::class,
        ProyectoSolar::class,
        Cliente::class,
        Instalador::class
    ],
    version = 8,                   // SUBE 1 LA VERSIÃ“N EN CADA CAMBIO DE APPDB(antes 7)
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productoDao(): ProductoDao
    abstract fun proyectoDao(): ProyectoDao
    abstract fun clienteDao(): ClienteDao
    abstract fun instaladorDao(): InstaladorDao


    //companion object
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "green_solar_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

