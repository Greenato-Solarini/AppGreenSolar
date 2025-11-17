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
import com.GreenatoSolarini.myapplicationjetpackcompose.ui.theme.MyApplicationJetpackComposeTheme
import com.GreenatoSolarini.myapplicationjetpackcompose.viewmodel.CotizacionViewModel
import com.GreenatoSolarini.myapplicationjetpackcompose.viewmodel.ProductosViewModel
import com.GreenatoSolarini.myapplicationjetpackcompose.viewmodel.ProductosViewModelFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationJetpackComposeTheme {

                val navController = rememberNavController()

                // -------- ROOM --------
                val context = LocalContext.current
                val db = DatabaseProvider.getDatabase(context)
                val repository = ProductoRepository(db.productoDao())
                val productosViewModel: ProductosViewModel =
                    viewModel(factory = ProductosViewModelFactory(repository))

                // -------- Cotización --------
                val cotizacionViewModel: CotizacionViewModel = viewModel()

                AppNavGraph(
                    navController = navController,
                    productosViewModel = productosViewModel,
                    cotizacionViewModel = cotizacionViewModel
                )
            }
        }
    }
}

@Composable
fun AppNavGraph(
    navController: NavHostController,
    productosViewModel: ProductosViewModel,
    cotizacionViewModel: CotizacionViewModel
) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {

        // ---------- HOME ----------
        composable("home") {
            HomeScreen(
                onNavigateToProductos = { navController.navigate("productos") },
                onNavigateToCotizacion = { navController.navigate("cotizacion") }
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

        // ---------- NUEVO PRODUCTO ----------
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
    }
}
