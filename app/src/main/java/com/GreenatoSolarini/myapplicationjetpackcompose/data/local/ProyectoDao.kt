package com.GreenatoSolarini.myapplicationjetpackcompose.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.GreenatoSolarini.myapplicationjetpackcompose.model.ProyectoSolar
import kotlinx.coroutines.flow.Flow

@Dao
interface ProyectoDao {

    @Query("SELECT * FROM proyectos ORDER BY id DESC")
    fun getAll(): Flow<List<ProyectoSolar>>

    @Query("SELECT * FROM proyectos WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): ProyectoSolar?

    @Insert
    suspend fun insert(proyecto: ProyectoSolar)

    @Update
    suspend fun update(proyecto: ProyectoSolar)

    @Query("DELETE FROM proyectos WHERE id = :id")
    suspend fun deleteById(id: Int)
}