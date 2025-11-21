package com.GreenatoSolarini.myapplicationjetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.GreenatoSolarini.myapplicationjetpackcompose.data.local.DatabaseProvider
import com.GreenatoSolarini.myapplicationjetpackcompose.repository.ClienteRepository
import com.GreenatoSolarini.myapplicationjetpackcompose.repository.ProductoRepository
import com.GreenatoSolarini.myapplicationjetpackcompose.repository.ProyectoRepository
import com.GreenatoSolarini.myapplicationjetpackcompose.ui.screens.clientes.ClienteDetailScreen
import com.GreenatoSolarini.myapplicationjetpackcompose.ui.screens.clientes.ClientesScreen
import com.GreenatoSolarini.myapplicationjetpackcompose.ui.screens.clientes.EditarClienteScreen
import com.GreenatoSolarini.myapplicationjetpackcompose.ui.screens.clientes.NuevoClienteScreen
import com.GreenatoSolarini.myapplicationjetpackcompose.ui.screens.cotizaciones.CotizacionScreen
import com.GreenatoSolarini.myapplicationjetpackcompose.ui.screens.home.HomeScreen
import com.GreenatoSolarini.myapplicationjetpackcompose.ui.screens.productos.AddProductScreen
import com.GreenatoSolarini.myapplicationjetpackcompose.ui.screens.productos.EditarProductoScreen
import com.GreenatoSolarini.myapplicationjetpackcompose.ui.screens.productos.ProductoDetailScreen
import com.GreenatoSolarini.myapplicationjetpackcompose.ui.screens.productos.ProductosScreen
import com.GreenatoSolarini.myapplicationjetpackcompose.ui.screens.proyectos.EditarProyectoScreen
import com.GreenatoSolarini.myapplicationjetpackcompose.ui.screens.proyectos.NuevoProyectoScreen
import com.GreenatoSolarini.myapplicationjetpackcompose.ui.screens.proyectos.ProyectoDetailScreen
import com.GreenatoSolarini.myapplicationjetpackcompose.ui.screens.proyectos.ProyectosScreen
import com.GreenatoSolarini.myapplicationjetpackcompose.ui.theme.MyApplicationJetpackComposeTheme
import com.GreenatoSolarini.myapplicationjetpackcompose.viewmodel.ClientesViewModel
import com.GreenatoSolarini.myapplicationjetpackcompose.viewmodel.ClientesViewModelFactory
import com.GreenatoSolarini.myapplicationjetpackcompose.viewmodel.CotizacionViewModel
import com.GreenatoSolarini.myapplicationjetpackcompose.viewmodel.ProductosViewModel
import com.GreenatoSolarini.myapplicationjetpackcompose.viewmodel.ProductosViewModelFactory
import com.GreenatoSolarini.myapplicationjetpackcompose.viewmodel.ProyectosViewModel
import com.GreenatoSolarini.myapplicationjetpackcompose.viewmodel.ProyectosViewModelFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationJetpackComposeTheme {

                val navController = rememberNavController()

                // -------- ROOM (Base de datos) --------
                val context = LocalContext.current
                val db = DatabaseProvider.getDatabase(context)

                // -------- Repositorios --------
                val productoRepository = ProductoRepository(db.productoDao())
                val proyectoRepository = ProyectoRepository(db.proyectoDao())
                val clienteRepository = ClienteRepository(db.clienteDao())

                // -------- ViewModels --------
                val productosViewModel: ProductosViewModel =
                    viewModel(factory = ProductosViewModelFactory(productoRepository))

                val proyectosViewModel: ProyectosViewModel =
                    viewModel(factory = ProyectosViewModelFactory(proyectoRepository))

                val clientesViewModel: ClientesViewModel =
                    viewModel(factory = ClientesViewModelFactory(clienteRepository))

                val cotizacionViewModel: CotizacionViewModel = viewModel()

                AppNavGraph(
                    navController = navController,
                    productosViewModel = productosViewModel,
                    cotizacionViewModel = cotizacionViewModel,
                    proyectosViewModel = proyectosViewModel,
                    clientesViewModel = clientesViewModel
                )
            }
        }
    }
}

@Composable
fun AppNavGraph(
    navController: NavHostController,
    productosViewModel: ProductosViewModel,
    cotizacionViewModel: CotizacionViewModel,
    proyectosViewModel: ProyectosViewModel,
    clientesViewModel: ClientesViewModel
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
                onNavigateToClientes = { navController.navigate("clientes") }
            )
        }

        // ---------- PRODUCTOS ----------
        composable("productos") {
            ProductosScreen(
                viewModel = productosViewModel,
                onNavigateToAdd = { navController.navigate("addProducto") },
                onNavigateToEdit = { id ->
                    navController.navigate("productoEditar/$id")
                },
                onNavigateToDetail = { id ->
                    navController.navigate("productoDetalle/$id")
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable("addProducto") {
            AddProductScreen(
                viewModel = productosViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable("productoEditar/{id}") { backStackEntry ->
            val idParam = backStackEntry.arguments?.getString("id")?.toIntOrNull()
            val producto = idParam?.let { productosViewModel.obtenerProductoPorId(it) }

            if (producto != null) {
                EditarProductoScreen(
                    producto = producto,
                    viewModel = productosViewModel,
                    onBack = { navController.popBackStack() }
                )
            } else {
                navController.popBackStack()
            }
        }

        composable("productoDetalle/{id}") { backStackEntry ->
            val idParam = backStackEntry.arguments?.getString("id")?.toIntOrNull()
            val producto = idParam?.let { productosViewModel.obtenerProductoPorId(it) }

            if (producto != null) {
                ProductoDetailScreen(
                    producto = producto,
                    viewModel = productosViewModel,
                    onBack = { navController.popBackStack() }
                )
            } else {
                navController.popBackStack()
            }
        }

        // ---------- COTIZACIÓN ----------
        composable("cotizacion") {
            CotizacionScreen(
                viewModel = cotizacionViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        // ---------- PROYECTOS ----------
        composable("proyectos") {
            ProyectosScreen(
                viewModel = proyectosViewModel,
                onProyectoClick = { id ->
                    navController.navigate("proyectoDetalle/$id")
                },
                onProyectoEdit = { id ->
                    navController.navigate("proyectoEditar/$id")
                },
                onProyectoDelete = { id ->
                    proyectosViewModel.eliminarProyectoPorId(id)
                },
                onNavigateToNuevo = { navController.navigate("proyectoNuevo") },
                onBack = { navController.popBackStack() }
            )
        }

        composable("proyectoNuevo") {
            NuevoProyectoScreen(
                proyectosViewModel = proyectosViewModel,
                clientesViewModel = clientesViewModel,
                onBack = { navController.popBackStack() }
            )
        }


        composable("proyectoEditar/{id}") { backStackEntry ->
            val idParam = backStackEntry.arguments?.getString("id")?.toIntOrNull()
            val proyecto = idParam?.let { proyectosViewModel.obtenerProyectoPorId(it) }

            if (proyecto != null) {
                EditarProyectoScreen(
                    proyecto = proyecto,
                    viewModel = proyectosViewModel,
                    onBack = { navController.popBackStack() }
                )
            } else {
                navController.popBackStack()
            }
        }

        composable("proyectoDetalle/{id}") { backStackEntry ->
            val idParam = backStackEntry.arguments?.getString("id")?.toIntOrNull()
            val proyecto = idParam?.let { proyectosViewModel.obtenerProyectoPorId(it) }

            if (proyecto != null) {
                ProyectoDetailScreen(
                    proyecto = proyecto,
                    viewModel = proyectosViewModel,
                    onBack = { navController.popBackStack() }
                )
            } else {
                navController.popBackStack()
            }
        }

        // ---------- CLIENTES ----------
        composable("clientes") {
            ClientesScreen(
                viewModel = clientesViewModel,
                onClienteClick = { id ->
                    navController.navigate("clienteDetalle/$id")
                },
                onClienteEdit = { id ->
                    navController.navigate("clienteEditar/$id")
                },
                onClienteDelete = { id ->
                    val cliente = clientesViewModel.obtenerClientePorId(id)
                    if (cliente != null) {
                        clientesViewModel.eliminarCliente(cliente)
                    }
                },
                onNavigateToNuevo = { navController.navigate("clienteNuevo") },
                onBack = { navController.popBackStack() }
            )
        }

        composable("clienteNuevo") {
            NuevoClienteScreen(
                viewModel = clientesViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable("clienteEditar/{id}") { backStackEntry ->
            val idParam = backStackEntry.arguments?.getString("id")?.toIntOrNull()
            val cliente = idParam?.let { clientesViewModel.obtenerClientePorId(it) }

            if (cliente != null) {
                EditarClienteScreen(
                    cliente = cliente,
                    viewModel = clientesViewModel,
                    onBack = { navController.popBackStack() }
                )
            } else {
                navController.popBackStack()
            }
        }

        composable("clienteDetalle/{id}") { backStackEntry ->
            val idParam = backStackEntry.arguments?.getString("id")?.toIntOrNull()
            val cliente = idParam?.let { clientesViewModel.obtenerClientePorId(it) }

            if (cliente != null) {
                ClienteDetailScreen(
                    cliente = cliente,
                    onBack = { navController.popBackStack() }
                )
            } else {
                navController.popBackStack()
            }
        }
    }
}
