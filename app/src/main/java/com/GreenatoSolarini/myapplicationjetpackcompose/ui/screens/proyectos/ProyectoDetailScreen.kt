package com.GreenatoSolarini.myapplicationjetpackcompose.ui.screens.proyectos

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.GreenatoSolarini.myapplicationjetpackcompose.viewmodel.ProyectosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProyectoDetailScreen(
    proyecto: ProyectoSolar,
    viewModel: ProyectosViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    // Cargar foto previa si existe
    var fotoProyecto by remember {
        mutableStateOf<Bitmap?>(viewModel.obtenerFotoProyecto(proyecto.id))
    }
    var permisoDenegado by remember { mutableStateOf(false) }

    // Launcher para cámara
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            fotoProyecto = bitmap
            viewModel.guardarFotoProyecto(proyecto.id, bitmap)
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

    // Launcher para galería (selector de imágenes)
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            val bitmap: Bitmap? = try {
                if (Build.VERSION.SDK_INT < 28) {
                    @Suppress("DEPRECATION")
                    MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                } else {
                    val source = ImageDecoder.createSource(context.contentResolver, uri)
                    ImageDecoder.decodeBitmap(source)
                }
            } catch (e: Exception) {
                null
            }

            bitmap?.let {
                fotoProyecto = it
                viewModel.guardarFotoProyecto(proyecto.id, it)
            }
        }
    }

    fun manejarClickCamara() {
        val permissionCheck = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        )

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            takePictureLauncher.launch(null)
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    fun manejarClickGaleria() {
        // Abre el selector de imágenes del sistema (no necesita permisos de almacenamiento)
        pickImageLauncher.launch("image/*")
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

            // BOTONES RECURSOS NATIVOS
            Button(
                onClick = { manejarClickCamara() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Tomar foto del proyecto (Cámara)")
            }

            Button(
                onClick = { manejarClickGaleria() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Elegir foto desde galería")
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
                    text = "Imagen asociada al proyecto:",
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
