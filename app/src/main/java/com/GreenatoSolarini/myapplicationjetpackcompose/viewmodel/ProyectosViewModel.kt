package com.GreenatoSolarini.myapplicationjetpackcompose.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.GreenatoSolarini.myapplicationjetpackcompose.model.ProyectoSolar
import com.GreenatoSolarini.myapplicationjetpackcompose.repository.ProyectoRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow

class ProyectosViewModel : ViewModel() {

    private val repository = ProyectoRepository()

    val proyectos: StateFlow<List<ProyectoSolar>> = repository.proyectos

    // Mapa idProyecto -> foto
    private val _fotosProyecto = MutableStateFlow<Map<Int, Bitmap?>>(emptyMap())

    fun obtenerProyectoPorId(id: Int): ProyectoSolar? {
        return repository.obtenerProyectoPorId(id)
    }

    fun crearProyecto(
        nombre: String,
        cliente: String,
        direccion: String,
        estado: String
    ) {
        repository.crearProyecto(
            nombre = nombre,
            cliente = cliente,
            direccion = direccion,
            estado = estado
        )
    }

    fun actualizarProyecto(proyecto: ProyectoSolar) {
        repository.actualizarProyecto(proyecto)
    }

    fun eliminarProyecto(id: Int) {
        repository.eliminarProyecto(id)
    }

    fun guardarFotoProyecto(id: Int, bitmap: Bitmap) {
        val actual = _fotosProyecto.value.toMutableMap()
        actual[id] = bitmap
        _fotosProyecto.value = actual
    }

    fun obtenerFotoProyecto(id: Int): Bitmap? {
        return _fotosProyecto.value[id]
    }
}
