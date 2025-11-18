package com.GreenatoSolarini.myapplicationjetpackcompose.ui.screens.proyectos

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.GreenatoSolarini.myapplicationjetpackcompose.model.ProyectoSolar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProyectoDetailScreen(
    proyecto: ProyectoSolar,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    // Estado para almacenar la foto tomada
    var fotoProyecto by remember { mutableStateOf<Bitmap?>(null) }
    var permisoDenegado by remember { mutableStateOf(false) }

    // Launcher para tomar la foto (preview en Bitmap)
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            fotoProyecto = bitmap
        }
    }

    // Launcher para pedir permiso de cámara
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            permisoDenegado = false
            takePictureLauncher.launch(null)
        } else {
            permisoDenegado = true
        }

    }

    fun manejarClickRecursoNativo() {
        val permissionCheck = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        )

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            // Ya tiene permiso → abrir cámara
            takePictureLauncher.launch(null)
        } else {
            // Pedir permiso
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }

    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(proyecto.nombre) }
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

            // -------- DATOS DEL PROYECTO --------
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

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Avance de instalación:", style = MaterialTheme.typography.bodyMedium)
                    LinearProgressIndicator(
                        progress = (proyecto.avancePorcentaje / 100f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp)
                    )
                    Text("${proyecto.avancePorcentaje} % completado")
                }
            }

            // -------- MONITOREO --------
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

            // -------- RECURSO NATIVO: CÁMARA --------
            Button(
                onClick = { manejarClickRecursoNativo() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Tomar foto del proyecto")
            }

            if (permisoDenegado) {
                Text(
                    text = "Permiso de cámara denegado. Actívalo en ajustes para usar esta función.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            fotoProyecto?.let { bitmap ->
                Text(
                    text = "Última foto registrada:",
                    style = MaterialTheme.typography.bodyMedium
                )
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Foto del proyecto",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }

            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver")
            }
        }
    }
}
