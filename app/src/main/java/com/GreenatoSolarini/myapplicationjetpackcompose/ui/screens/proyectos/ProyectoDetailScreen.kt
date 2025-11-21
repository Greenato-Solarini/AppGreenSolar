package com.GreenatoSolarini.myapplicationjetpackcompose.ui.screens.proyectos

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.GreenatoSolarini.myapplicationjetpackcompose.model.ProyectoSolar
import com.GreenatoSolarini.myapplicationjetpackcompose.viewmodel.ProyectosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProyectoDetailScreen(
    proyecto: ProyectoSolar,
    viewModel: ProyectosViewModel,
    onBack: () -> Unit
) {
    // Solo leemos la foto guardada (si existe)
    var fotoProyecto by remember {
        mutableStateOf(viewModel.obtenerFotoProyecto(proyecto.id))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(proyecto.nombre) },
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // DATOS DEL PROYECTO
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White,
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("Cliente: ${proyecto.cliente}", style = MaterialTheme.typography.bodyLarge)
                    Text("Dirección: ${proyecto.direccion}", style = MaterialTheme.typography.bodyMedium)
                    Text("Estado: ${proyecto.estado}", style = MaterialTheme.typography.bodyMedium)
                }
            }

            // MONITOREO
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White,
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "Monitoreo de consumo",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text("Producción actual: ${proyecto.produccionActualW} W")
                    Text("Consumo actual: ${proyecto.consumoActualW} W")
                    Text("Ahorro estimado hoy: ${proyecto.ahorroHoyClp} CLP")
                }
            }

            // FOTO SOLO LECTURA
            fotoProyecto?.let { bitmap ->
                Text(
                    text = "Imagen asociada al proyecto:",
                    style = MaterialTheme.typography.titleMedium
                )
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Foto del proyecto",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }
        }
    }
}
