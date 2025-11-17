package com.GreenatoSolarini.myapplicationjetpackcompose.ui.screens.productos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.GreenatoSolarini.myapplicationjetpackcompose.model.Producto
import com.GreenatoSolarini.myapplicationjetpackcompose.viewmodel.ProductosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductosScreen(
    viewModel: ProductosViewModel,
    onNavigateToAdd: () -> Unit,
    onBack: () -> Unit
) {
    val productos by viewModel.productos.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Productos solares") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToAdd) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar producto"
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            // ---------- LISTA ----------
            if (productos.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay productos registrados todavía.")
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    items(productos) { producto ->
                        ProductoItem(
                            producto = producto,
                            onDelete = { viewModel.eliminarProducto(producto) }
                        )
                    }
                }
            }

            // ---------- BOTÓN VOLVER ----------
            Button(
                onClick = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Volver")
            }
        }
    }
}

@Composable
fun ProductoItem(
    producto: Producto,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = producto.nombre, style = MaterialTheme.typography.titleMedium)
                Text(text = "Precio: ${producto.precio} CLP")
                if (producto.descripcion.isNotBlank()) {
                    Text(
                        text = producto.descripcion,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar"
                )
            }
        }
    }
}
