package com.GreenatoSolarini.myapplicationjetpackcompose.ui.screens.productos

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.GreenatoSolarini.myapplicationjetpackcompose.model.Producto
import com.GreenatoSolarini.myapplicationjetpackcompose.viewmodel.ProductosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarProductoScreen(
    producto: Producto,
    viewModel: ProductosViewModel,
    onBack: () -> Unit
) {
    var nombre by remember { mutableStateOf(producto.nombre) }
    var precioText by remember { mutableStateOf(producto.precio.toString()) }
    var descripcion by remember { mutableStateOf(producto.descripcion) }
    var showError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar producto") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            OutlinedTextField(
                value = nombre,
                onValueChange = {
                    nombre = it
                    if (showError) showError = false
                },
                label = { Text("Nombre del producto") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = precioText,
                onValueChange = {
                    precioText = it
                    if (showError) showError = false
                },
                label = { Text("Precio (CLP)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción (opcional)") },
                modifier = Modifier.fillMaxWidth()
            )

            if (showError) {
                Text(
                    text = "Revisa el nombre y el precio (debe ser un número mayor a 0).",
                    color = MaterialTheme.colorScheme.error
                )
            }

            Button(
                onClick = {
                    val precio = precioText.toIntOrNull()
                    if (nombre.isBlank() || precio == null || precio <= 0) {
                        showError = true
                    } else {
                        val actualizado = producto.copy(
                            nombre = nombre,
                            precio = precio,
                            descripcion = descripcion
                        )
                        viewModel.actualizarProducto(actualizado)
                        onBack()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar cambios")
            }

            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancelar")
            }
        }
    }
}
