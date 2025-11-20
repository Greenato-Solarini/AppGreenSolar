package com.GreenatoSolarini.myapplicationjetpackcompose.data.local

import android.content.Context
import androidx.room.Room

object DatabaseProvider {

    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "greensolar_db"
            )
                .fallbackToDestructiveMigration() // para no complicarnos con migraciones ahora
                .build()
                .also { INSTANCE = it }
        }
    }
}
