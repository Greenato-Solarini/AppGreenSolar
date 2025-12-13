package com.greenatosolarini.myapplicationjetpackcompose.ui.screens.productos

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.greenatosolarini.myapplicationjetpackcompose.model.Producto
import com.greenatosolarini.myapplicationjetpackcompose.viewmodel.ProductosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductoDetailScreen(
    producto: Producto,
    viewModel: ProductosViewModel,
    onBack: () -> Unit
) {
    var fotoProducto by remember {
        mutableStateOf(viewModel.obtenerFotoProducto(producto.id))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(producto.nombre) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
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

            Text(
                text = "Precio: ${producto.precio} CLP",
                style = MaterialTheme.typography.titleMedium
            )

            if (producto.descripcion.isNotBlank()) {
                Text(
                    text = "DescripciÃ³n:",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = producto.descripcion,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                Text("Sin descripcion.")
            }

            if (fotoProducto != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Imagen del producto:",
                    style = MaterialTheme.typography.titleMedium
                )
                Image(
                    bitmap = fotoProducto!!.asImageBitmap(),
                    contentDescription = "Foto del producto",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                )
            }
        }
    }
}

