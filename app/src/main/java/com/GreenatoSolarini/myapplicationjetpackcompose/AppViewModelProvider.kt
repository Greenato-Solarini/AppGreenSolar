package com.greenatosolarini.myapplicationjetpackcompose

import android.content.Context
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.Composable
import com.greenatosolarini.myapplicationjetpackcompose.data.local.DatabaseProvider
import com.greenatosolarini.myapplicationjetpackcompose.repository.*
import com.greenatosolarini.myapplicationjetpackcompose.viewmodel.*

/**
 * Proveedor centralizado de ViewModels
 * 
 * Este objeto se encarga de:
 * - Inicializar la base de datos
 * - Crear los repositorios
 * - Instanciar los ViewModels con sus factories
 * 
 * Ventajas:
 * - MainActivity queda limpio (8-10 líneas)
 * - Reutilizable en múltiples Activities
 * - Fácil de testear
 * - Preparado para Dependency Injection (Hilt)
 */
@Composable
fun rememberAppViewModels(context: Context): AppViewModels {
    // Inicializar base de datos
    val db = DatabaseProvider.getDatabase(context)

    // Crear repositorios
    val productoRepository = ProductoRepository(db.productoDao())
    val proyectoRepository = ProyectoRepository(db.proyectoDao())
    val clienteRepository = ClienteRepository(db.clienteDao())
    val instaladorRepository = InstaladorRepository(db.instaladorDao())

    // Crear ViewModels con sus factories
    val productosViewModel: ProductosViewModel =
        viewModel(factory = ProductosViewModelFactory(productoRepository))

    val proyectosViewModel: ProyectosViewModel =
        viewModel(factory = ProyectosViewModelFactory(proyectoRepository))

    val clientesViewModel: ClientesViewModel =
        viewModel(factory = ClientesViewModelFactory(clienteRepository))

    val instaladoresViewModel: InstaladoresViewModel =
        viewModel(factory = InstaladoresViewModelFactory(instaladorRepository))

    val cotizacionViewModel: CotizacionViewModel = viewModel()

    return AppViewModels(
        productosViewModel = productosViewModel,
        proyectosViewModel = proyectosViewModel,
        clientesViewModel = clientesViewModel,
        instaladoresViewModel = instaladoresViewModel,
        cotizacionViewModel = cotizacionViewModel
    )
}

/**
 * Data class que contiene todos los ViewModels de la aplicación
 */
data class AppViewModels(
    val productosViewModel: ProductosViewModel,
    val proyectosViewModel: ProyectosViewModel,
    val clientesViewModel: ClientesViewModel,
    val instaladoresViewModel: InstaladoresViewModel,
    val cotizacionViewModel: CotizacionViewModel
)
