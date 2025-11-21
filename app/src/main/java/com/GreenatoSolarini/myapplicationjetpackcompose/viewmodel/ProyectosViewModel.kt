package com.GreenatoSolarini.myapplicationjetpackcompose.viewmodel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.GreenatoSolarini.myapplicationjetpackcompose.model.ProyectoSolar
import com.GreenatoSolarini.myapplicationjetpackcompose.repository.ProyectoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class ProyectosViewModel(
    private val repository: ProyectoRepository
) : ViewModel() {

    val proyectos = repository.proyectos.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private fun bitmapToBytes(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
        return stream.toByteArray()
    }

    private fun bytesToBitmap(bytes: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    fun agregarProyecto(proyecto: ProyectoSolar) {
        viewModelScope.launch {
            repository.insertar(proyecto)
        }
    }

    fun actualizarProyecto(proyecto: ProyectoSolar) {
        viewModelScope.launch {
            repository.actualizar(proyecto)
        }
    }

    fun eliminarProyectoPorId(id: Int) {
        viewModelScope.launch {
            repository.eliminarPorId(id)
        }
    }

    fun obtenerProyectoPorId(id: Int): ProyectoSolar? {
        return proyectos.value.firstOrNull { it.id == id }
    }

    // -------- PERSISTENCIA DE FOTO --------

    fun guardarFotoProyecto(id: Int, bitmap: Bitmap) {
        viewModelScope.launch {
            val proyectoActual = repository.obtenerPorId(id) ?: return@launch
            val bytes = bitmapToBytes(bitmap)
            val actualizado = proyectoActual.copy(foto = bytes)
            repository.actualizar(actualizado)
        }
    }

    fun obtenerFotoProyecto(id: Int): Bitmap? {
        val proyecto = proyectos.value.firstOrNull { it.id == id } ?: return null
        val bytes = proyecto.foto ?: return null
        return bytesToBitmap(bytes)
    }
}
