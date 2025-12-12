package com.greenatosolarini.myapplicationjetpackcompose


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
import androidx.compose.runtime.collectAsState
import com.greenatosolarini.myapplicationjetpackcompose.data.local.DatabaseProvider
import com.greenatosolarini.myapplicationjetpackcompose.repository.*
import com.greenatosolarini.myapplicationjetpackcompose.ui.screens.clientes.*
import com.greenatosolarini.myapplicationjetpackcompose.ui.screens.cotizaciones.CotizacionScreen
import com.greenatosolarini.myapplicationjetpackcompose.ui.screens.home.HomeScreen
import com.greenatosolarini.myapplicationjetpackcompose.ui.screens.instaladores.InstaladoresScreen
import com.greenatosolarini.myapplicationjetpackcompose.ui.screens.productos.*
import com.greenatosolarini.myapplicationjetpackcompose.ui.screens.proyectos.*
import com.greenatosolarini.myapplicationjetpackcompose.ui.screens.instaladores.*
import com.greenatosolarini.myapplicationjetpackcompose.ui.theme.MyApplicationJetpackComposeTheme
import com.greenatosolarini.myapplicationjetpackcompose.viewmodel.*

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationJetpackComposeTheme {

                val navController = rememberNavController()
                val context = LocalContext.current
                val db = DatabaseProvider.getDatabase(context)

                // -------- Repositorios --------
                val productoRepository = ProductoRepository(db.productoDao())
                val proyectoRepository = ProyectoRepository(db.proyectoDao())
                val clienteRepository = ClienteRepository(db.clienteDao())
                val instaladorRepository = InstaladorRepository(db.instaladorDao())

                // -------- ViewModels --------
                val productosViewModel: ProductosViewModel =
                    viewModel(factory = ProductosViewModelFactory(productoRepository))

                val proyectosViewModel: ProyectosViewModel =
                    viewModel(factory = ProyectosViewModelFactory(proyectoRepository))

                val clientesViewModel: ClientesViewModel =
                    viewModel(factory = ClientesViewModelFactory(clienteRepository))

                val instaladoresViewModel: InstaladoresViewModel =
                    viewModel(factory = InstaladoresViewModelFactory(instaladorRepository))

                val cotizacionViewModel: CotizacionViewModel = viewModel()

                AppNavGraph(
                    navController = navController,
                    productosViewModel = productosViewModel,
                    cotizacionViewModel = cotizacionViewModel,
                    proyectosViewModel = proyectosViewModel,
                    clientesViewModel = clientesViewModel,
                    instaladoresViewModel = instaladoresViewModel
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
    clientesViewModel: ClientesViewModel,
    instaladoresViewModel: InstaladoresViewModel
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
                viewModel = instaladoresViewModel,
                onNavigateToNuevo = { navController.navigate("instaladorNuevo") },
                onInstaladorClick = { id -> navController.navigate("instaladorDetalle/$id") },
                onInstaladorEdit = { id -> navController.navigate("instaladorEditar/$id") },
                onBack = { navController.popBackStack() }
            )
        }


        composable("instaladorNuevo") {
            NuevoInstaladorScreen(
                viewModel = instaladoresViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable("instaladorEditar/{id}") { backStackEntry ->
            val idParam = backStackEntry.arguments?.getString("id")?.toIntOrNull()
            val instalador = idParam?.let { instaladoresViewModel.obtenerInstaladorPorId(it) }

            if (instalador != null) {
                EditarInstaladorScreen(
                    instaladorInicial = instalador,
                    viewModel = instaladoresViewModel,
                    onBack = { navController.popBackStack() }
                )
            } else {
                navController.popBackStack()
            }
        }

        composable("instaladorDetalle/{id}") { backStackEntry ->
            val idParam = backStackEntry.arguments?.getString("id")?.toIntOrNull()
            val instalador = idParam?.let { instaladoresViewModel.obtenerInstaladorPorId(it) }

            if (instalador != null) {
                // 👇 Obtenemos los proyectos y filtramos los asignados a este instalador
                val proyectosState = proyectosViewModel.proyectos.collectAsState()
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
                viewModel = clientesViewModel,
                onNavigateToNuevo = { navController.navigate("clienteNuevo") },
                onClienteClick = { id -> navController.navigate("clienteDetalle/$id") },
                onClienteEdit = { id -> navController.navigate("clienteEditar/$id") },
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
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
            val cliente = id?.let { clientesViewModel.obtenerClientePorId(it) }

            if (cliente != null) {
                EditarClienteScreen(
                    clienteInicial = cliente,
                    viewModel = clientesViewModel,
                    onBack = { navController.popBackStack() }
                )
            } else navController.popBackStack()
        }

        composable("clienteDetalle/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
            val cliente = id?.let { clientesViewModel.obtenerClientePorId(it) }

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
                viewModel = productosViewModel,
                onNavigateToAdd = { navController.navigate("addProducto") },
                onNavigateToEdit = { id -> navController.navigate("productoEditar/$id") },
                onNavigateToDetail = { id -> navController.navigate("productoDetalle/$id") },
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
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
            val producto = id?.let { productosViewModel.obtenerProductoPorId(it) }

            if (producto != null) {
                EditarProductoScreen(
                    producto = producto,
                    viewModel = productosViewModel,
                    onBack = { navController.popBackStack() }
                )
            } else navController.popBackStack()
        }

        composable("productoDetalle/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
            val producto = id?.let { productosViewModel.obtenerProductoPorId(it) }

            if (producto != null) {
                ProductoDetailScreen(
                    producto = producto,
                    viewModel = productosViewModel,
                    onBack = { navController.popBackStack() }
                )
            } else navController.popBackStack()
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
                clientesViewModel = clientesViewModel,
                instaladoresViewModel = instaladoresViewModel,
                onProyectoClick = { id -> navController.navigate("proyectoDetalle/$id") },
                onProyectoEdit = { id -> navController.navigate("proyectoEditar/$id") },
                onProyectoDelete = { id -> proyectosViewModel.eliminarProyectoPorId(id) },
                onNavigateToNuevo = { navController.navigate("proyectoNuevo") },
                onBack = { navController.popBackStack() }
            )
        }

        composable("proyectoNuevo") {
            NuevoProyectoScreen(
                viewModel = proyectosViewModel,
                clientesViewModel = clientesViewModel,
                instaladoresViewModel = instaladoresViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable("proyectoEditar/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
            val proyecto = id?.let { proyectosViewModel.obtenerProyectoPorId(it) }
x
            if (proyecto != null) {
                EditarProyectoScreen(
                    proyecto = proyecto,
                    viewModel = proyectosViewModel,
                    onBack = { navController.popBackStack() }
                )
            } else navController.popBackStack()
        }

        composable("proyectoDetalle/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
            val proyecto = id?.let { proyectosViewModel.obtenerProyectoPorId(it) }

            if (proyecto != null) {

                val clienteNombre = clientesViewModel
                    .obtenerClientePorId(proyecto.clienteId)
                    ?.nombre ?: "Cliente no encontrado"

                val instaladorNombre = proyecto.instaladorId?.let {
                    instaladoresViewModel.obtenerInstaladorPorId(it)?.nombre
                } ?: "Sin instalador asignado"

                ProyectoDetailScreen(
                    proyecto = proyecto,
                    clienteNombre = clienteNombre,
                    instaladorNombre = instaladorNombre,
                    viewModel = proyectosViewModel,
                    onBack = { navController.popBackStack() }
                )
            } else navController.popBackStack()
        }
    }
}
