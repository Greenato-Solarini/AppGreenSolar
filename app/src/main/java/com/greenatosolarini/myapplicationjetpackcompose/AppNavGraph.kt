package com.greenatosolarini.myapplicationjetpackcompose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.greenatosolarini.myapplicationjetpackcompose.ui.screens.clientes.*
import com.greenatosolarini.myapplicationjetpackcompose.ui.screens.cotizaciones.CotizacionScreen
import com.greenatosolarini.myapplicationjetpackcompose.ui.screens.home.HomeScreen
import com.greenatosolarini.myapplicationjetpackcompose.ui.screens.instaladores.*
import com.greenatosolarini.myapplicationjetpackcompose.ui.screens.productos.*
import com.greenatosolarini.myapplicationjetpackcompose.ui.screens.proyectos.*
import com.greenatosolarini.myapplicationjetpackcompose.viewmodel.*

/**
 * Definición del grafo de navegación de la aplicación
 * 
 * Contiene todas las rutas y composables de la app
 */
@Composable
fun AppNavGraph(
    navController: NavHostController,
    viewModels: AppViewModels
) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {

        // ---------- HOME ----------
        composable("home") {
            HomeScreen(
                onNavigateToProductos = { navController.navigate("productos") },
                onNavigateToCotizacion = { navController.navigate("cotizacion") },
                onNavigateToProyectos = { navController.navigate("proyectos") },
                onNavigateToClientes = { navController.navigate("clientes") },
                onNavigateToInstaladores = { navController.navigate("instaladores") }
            )
        }

        // ---------- INSTALADORES ----------
        composable("instaladores") {
            InstaladoresScreen(
                viewModel = viewModels.instaladoresViewModel,
                onNavigateToNuevo = { navController.navigate("instaladorNuevo") },
                onInstaladorClick = { id -> navController.navigate("instaladorDetalle/$id") },
                onInstaladorEdit = { id -> navController.navigate("instaladorEditar/$id") },
                onBack = { navController.popBackStack() }
            )
        }

        composable("instaladorNuevo") {
            NuevoInstaladorScreen(
                viewModel = viewModels.instaladoresViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable("instaladorEditar/{id}") { backStackEntry ->
            val idParam = backStackEntry.arguments?.getString("id")?.toIntOrNull()
            val instalador = idParam?.let { viewModels.instaladoresViewModel.obtenerInstaladorPorId(it) }

            if (instalador != null) {
                EditarInstaladorScreen(
                    instaladorInicial = instalador,
                    viewModel = viewModels.instaladoresViewModel,
                    onBack = { navController.popBackStack() }
                )
            } else {
                navController.popBackStack()
            }
        }

        composable("instaladorDetalle/{id}") { backStackEntry ->
            val idParam = backStackEntry.arguments?.getString("id")?.toIntOrNull()
            val instalador = idParam?.let { viewModels.instaladoresViewModel.obtenerInstaladorPorId(it) }

            if (instalador != null) {
                val proyectosState = viewModels.proyectosViewModel.proyectos.collectAsState()
                val proyectosAsignados = proyectosState.value.filter { it.instaladorId == instalador.id }

                InstaladorDetailScreen(
                    instalador = instalador,
                    proyectosAsignados = proyectosAsignados,
                    onBack = { navController.popBackStack() }
                )
            } else {
                navController.popBackStack()
            }
        }

        // ---------- CLIENTES ----------
        composable("clientes") {
            ClientesScreen(
                viewModel = viewModels.clientesViewModel,
                onNavigateToNuevo = { navController.navigate("clienteNuevo") },
                onClienteClick = { id -> navController.navigate("clienteDetalle/$id") },
                onClienteEdit = { id -> navController.navigate("clienteEditar/$id") },
                onBack = { navController.popBackStack() }
            )
        }

        composable("clienteNuevo") {
            NuevoClienteScreen(
                viewModel = viewModels.clientesViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable("clienteEditar/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
            val cliente = id?.let { viewModels.clientesViewModel.obtenerClientePorId(it) }

            if (cliente != null) {
                EditarClienteScreen(
                    clienteInicial = cliente,
                    viewModel = viewModels.clientesViewModel,
                    onBack = { navController.popBackStack() }
                )
            } else navController.popBackStack()
        }

        composable("clienteDetalle/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
            val cliente = id?.let { viewModels.clientesViewModel.obtenerClientePorId(it) }

            if (cliente != null) {
                ClienteDetailScreen(
                    cliente = cliente,
                    onBack = { navController.popBackStack() }
                )
            } else navController.popBackStack()
        }

        // ---------- PRODUCTOS ----------
        composable("productos") {
            ProductosScreen(
                viewModel = viewModels.productosViewModel,
                onNavigateToAdd = { navController.navigate("addProducto") },
                onNavigateToEdit = { id -> navController.navigate("productoEditar/$id") },
                onNavigateToDetail = { id -> navController.navigate("productoDetalle/$id") },
                onBack = { navController.popBackStack() }
            )
        }

        composable("addProducto") {
            AddProductScreen(
                viewModel = viewModels.productosViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable("productoEditar/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
            val producto = id?.let { viewModels.productosViewModel.obtenerProductoPorId(it) }

            if (producto != null) {
                EditarProductoScreen(
                    producto = producto,
                    viewModel = viewModels.productosViewModel,
                    onBack = { navController.popBackStack() }
                )
            } else navController.popBackStack()
        }

        composable("productoDetalle/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
            val producto = id?.let { viewModels.productosViewModel.obtenerProductoPorId(it) }

            if (producto != null) {
                ProductoDetailScreen(
                    producto = producto,
                    viewModel = viewModels.productosViewModel,
                    onBack = { navController.popBackStack() }
                )
            } else navController.popBackStack()
        }

        // ---------- COTIZACIÓN ----------
        composable("cotizacion") {
            CotizacionScreen(
                viewModel = viewModels.cotizacionViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        // ---------- PROYECTOS ----------
        composable("proyectos") {
            ProyectosScreen(
                viewModel = viewModels.proyectosViewModel,
                clientesViewModel = viewModels.clientesViewModel,
                instaladoresViewModel = viewModels.instaladoresViewModel,
                onProyectoClick = { id -> navController.navigate("proyectoDetalle/$id") },
                onProyectoEdit = { id -> navController.navigate("proyectoEditar/$id") },
                onProyectoDelete = { id -> viewModels.proyectosViewModel.eliminarProyectoPorId(id) },
                onNavigateToNuevo = { navController.navigate("proyectoNuevo") },
                onBack = { navController.popBackStack() }
            )
        }

        composable("proyectoNuevo") {
            NuevoProyectoScreen(
                viewModel = viewModels.proyectosViewModel,
                clientesViewModel = viewModels.clientesViewModel,
                instaladoresViewModel = viewModels.instaladoresViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable("proyectoEditar/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
            val proyecto = id?.let { viewModels.proyectosViewModel.obtenerProyectoPorId(it) }

            if (proyecto != null) {
                EditarProyectoScreen(
                    proyecto = proyecto,
                    viewModel = viewModels.proyectosViewModel,
                    onBack = { navController.popBackStack() }
                )
            } else navController.popBackStack()
        }

        composable("proyectoDetalle/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
            val proyecto = id?.let { viewModels.proyectosViewModel.obtenerProyectoPorId(it) }

            if (proyecto != null) {
                val clienteNombre = viewModels.clientesViewModel
                    .obtenerClientePorId(proyecto.clienteId)
                    ?.nombre ?: "Cliente no encontrado"

                val instaladorNombre = proyecto.instaladorId?.let {
                    viewModels.instaladoresViewModel.obtenerInstaladorPorId(it)?.nombre
                } ?: "Sin instalador asignado"

                ProyectoDetailScreen(
                    proyecto = proyecto,
                    clienteNombre = clienteNombre,
                    instaladorNombre = instaladorNombre,
                    viewModel = viewModels.proyectosViewModel,
                    onBack = { navController.popBackStack() }
                )
            } else navController.popBackStack()
        }
    }
}
