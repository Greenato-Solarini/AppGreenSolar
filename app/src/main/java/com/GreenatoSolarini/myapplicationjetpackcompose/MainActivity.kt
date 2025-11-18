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
import com.GreenatoSolarini.myapplicationjetpackcompose.repository.ProductoRepository
import com.GreenatoSolarini.myapplicationjetpackcompose.ui.screens.cotizaciones.CotizacionScreen
import com.GreenatoSolarini.myapplicationjetpackcompose.ui.screens.home.HomeScreen
import com.GreenatoSolarini.myapplicationjetpackcompose.ui.screens.productos.AddProductScreen
import com.GreenatoSolarini.myapplicationjetpackcompose.ui.screens.productos.ProductosScreen
import com.GreenatoSolarini.myapplicationjetpackcompose.ui.screens.proyectos.ProyectoDetailScreen
import com.GreenatoSolarini.myapplicationjetpackcompose.ui.screens.proyectos.ProyectosScreen
import com.GreenatoSolarini.myapplicationjetpackcompose.ui.theme.MyApplicationJetpackComposeTheme
import com.GreenatoSolarini.myapplicationjetpackcompose.viewmodel.CotizacionViewModel
import com.GreenatoSolarini.myapplicationjetpackcompose.viewmodel.ProductosViewModel
import com.GreenatoSolarini.myapplicationjetpackcompose.viewmodel.ProductosViewModelFactory
import com.GreenatoSolarini.myapplicationjetpackcompose.viewmodel.ProyectosViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationJetpackComposeTheme {

                val navController = rememberNavController()

                // -------- ROOM (Productos) --------
                val context = LocalContext.current
                val db = DatabaseProvider.getDatabase(context)
                val repository = ProductoRepository(db.productoDao())
                val productosViewModel: ProductosViewModel =
                    viewModel(factory = ProductosViewModelFactory(repository))

                // -------- Cotización --------
                val cotizacionViewModel: CotizacionViewModel = viewModel()

                // -------- Proyectos --------
                val proyectosViewModel: ProyectosViewModel = viewModel()

                AppNavGraph(
                    navController = navController,
                    productosViewModel = productosViewModel,
                    cotizacionViewModel = cotizacionViewModel,
                    proyectosViewModel = proyectosViewModel
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
    proyectosViewModel: ProyectosViewModel
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
                onNavigateToProyectos = { navController.navigate("proyectos") }
            )
        }

        // ---------- PRODUCTOS ----------
        composable("productos") {
            ProductosScreen(
                viewModel = productosViewModel,
                onNavigateToAdd = { navController.navigate("addProducto") },
                onBack = { navController.popBackStack() }
            )
        }

        composable("addProducto") {
            AddProductScreen(
                viewModel = productosViewModel,
                onBack = { navController.popBackStack() }
            )
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
                onBack = { navController.popBackStack() }
            )
        }

        // Detalle proyecto
        composable("proyectoDetalle/{id}") { backStackEntry ->
            val idParam = backStackEntry.arguments?.getString("id")?.toIntOrNull()
            val proyecto = idParam?.let { proyectosViewModel.obtenerProyectoPorId(it) }

            if (proyecto != null) {
                ProyectoDetailScreen(
                    proyecto = proyecto,
                    onBack = { navController.popBackStack() }
                )
            } else {
                navController.popBackStack()
            }
        }

    }
}
