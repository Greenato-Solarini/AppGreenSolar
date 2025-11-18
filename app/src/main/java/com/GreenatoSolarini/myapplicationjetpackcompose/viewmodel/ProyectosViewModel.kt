package com.GreenatoSolarini.myapplicationjetpackcompose.viewmodel

import androidx.lifecycle.ViewModel
import com.GreenatoSolarini.myapplicationjetpackcompose.model.ProyectoSolar
import com.GreenatoSolarini.myapplicationjetpackcompose.repository.ProyectoRepository
import kotlinx.coroutines.flow.StateFlow

class ProyectosViewModel : ViewModel() {

    private val repository = ProyectoRepository()

    val proyectos: StateFlow<List<ProyectoSolar>> = repository.proyectos

    fun obtenerProyectoPorId(id: Int): ProyectoSolar? {
        return repository.obtenerProyectoPorId(id)
    }
}
