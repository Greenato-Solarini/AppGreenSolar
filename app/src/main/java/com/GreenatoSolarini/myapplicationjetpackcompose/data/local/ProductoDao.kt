package com.GreenatoSolarini.myapplicationjetpackcompose.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.GreenatoSolarini.myapplicationjetpackcompose.model.Producto
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDao {

    @Query("SELECT * FROM productos ORDER BY id DESC")
    fun getAll(): Flow<List<Producto>>

    @Query("SELECT * FROM productos WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): Producto?

    @Insert
    suspend fun insert(producto: Producto)

    @Update
    suspend fun update(producto: Producto)

    @Delete
    suspend fun delete(producto: Producto)
}
