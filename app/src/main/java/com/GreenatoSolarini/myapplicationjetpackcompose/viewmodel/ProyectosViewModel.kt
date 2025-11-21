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

    // ---------- CRUD PROYECTOS ----------

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

    // ---------- FOTO (CÁMARA / GALERÍA) ----------

    fun guardarFotoProyecto(proyectoId: Int, bitmap: Bitmap) {
        viewModelScope.launch {
            val proyecto = obtenerProyectoPorId(proyectoId)
            if (proyecto != null) {
                val bytes = bitmapToByteArray(bitmap)
                val actualizado = proyecto.copy(foto = bytes)
                repository.actualizar(actualizado)
            }
        }
    }

    fun obtenerFotoProyecto(proyectoId: Int): Bitmap? {
        val proyecto = obtenerProyectoPorId(proyectoId)
        val data = proyecto?.foto ?: return null
        return BitmapFactory.decodeByteArray(data, 0, data.size)
    }

    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }
}
