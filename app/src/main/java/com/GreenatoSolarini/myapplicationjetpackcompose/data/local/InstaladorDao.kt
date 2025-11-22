package com.GreenatoSolarini.myapplicationjetpackcompose.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.GreenatoSolarini.myapplicationjetpackcompose.model.Instalador
import kotlinx.coroutines.flow.Flow

@Dao
interface InstaladorDao {

    @Query("SELECT * FROM instaladores ORDER BY nombre ASC")
    fun getAll(): Flow<List<Instalador>>

    @Query("SELECT * FROM instaladores WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): Instalador?

    @Insert
    suspend fun insert(instalador: Instalador)

    @Update
    suspend fun update(instalador: Instalador)

    @Delete
    suspend fun delete(instalador: Instalador)
}
