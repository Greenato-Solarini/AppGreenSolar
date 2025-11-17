package com.GreenatoSolarini.myapplicationjetpackcompose.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.GreenatoSolarini.myapplicationjetpackcompose.model.Producto
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(producto: Producto)

    @Delete
    suspend fun delete(producto: Producto)

    @Query("SELECT * FROM producto")
    fun getAll(): Flow<List<Producto>>

    @Query("SELECT * FROM producto WHERE id = :id")
    suspend fun getById(id: Int): Producto?
}
