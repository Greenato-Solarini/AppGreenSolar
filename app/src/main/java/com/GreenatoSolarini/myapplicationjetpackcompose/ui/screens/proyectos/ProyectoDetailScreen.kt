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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
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
    clienteNombre: String,
    viewModel: ProyectosViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    var fotoProyecto by remember {
        mutableStateOf<Bitmap?>(viewModel.obtenerFotoProyecto(proyecto.id))
    }
    var permisoDenegado by remember { mutableStateOf(false) }

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            fotoProyecto = bitmap
            viewModel.guardarFotoProyecto(proyecto.id, bitmap)
        }
    }

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
        pickImageLauncher.launch("image/*")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(proyecto.nombre) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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
                    Text("Cliente: $clienteNombre", style = MaterialTheme.typography.bodyLarge)
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
        }
    }
}
