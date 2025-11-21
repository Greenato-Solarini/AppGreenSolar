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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.GreenatoSolarini.myapplicationjetpackcompose.model.ProyectoSolar
import com.GreenatoSolarini.myapplicationjetpackcompose.viewmodel.ProyectosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarProyectoScreen(
    proyecto: ProyectoSolar,
    viewModel: ProyectosViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    var nombreProyecto by remember { mutableStateOf(proyecto.nombre) }
    var nombreCliente by remember { mutableStateOf(proyecto.cliente) }
    var direccion by remember { mutableStateOf(proyecto.direccion) }
    var estado by remember { mutableStateOf(proyecto.estado) }

    var showError by remember { mutableStateOf(false) }

    // Foto inicial desde ViewModel / Room
    var fotoProyecto by remember {
        mutableStateOf(viewModel.obtenerFotoProyecto(proyecto.id))
    }
    var permisoDenegado by remember { mutableStateOf(false) }

    // Cámara
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            fotoProyecto = bitmap
            viewModel.guardarFotoProyecto(proyecto.id, bitmap)
        }
    }

    // Permiso cámara
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

    // Galería
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

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar proyecto solar") },
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
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            OutlinedTextField(
                value = nombreProyecto,
                onValueChange = {
                    nombreProyecto = it
                    showError = false
                },
                label = { Text("Nombre del proyecto") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = nombreCliente,
                onValueChange = {
                    nombreCliente = it
                    showError = false
                },
                label = { Text("Nombre del cliente") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = direccion,
                onValueChange = {
                    direccion = it
                    showError = false
                },
                label = { Text("Dirección / Comuna") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = estado,
                onValueChange = { estado = it },
                label = { Text("Estado (En evaluación / En instalación / Operativo)") },
                modifier = Modifier.fillMaxWidth()
            )

            if (showError) {
                Text(
                    text = "Completa al menos nombre de proyecto, cliente y dirección.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // BOTONES FOTO
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

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (nombreProyecto.isBlank() ||
                        nombreCliente.isBlank() ||
                        direccion.isBlank()
                    ) {
                        showError = true
                    } else {
                        val actualizado = proyecto.copy(
                            nombre = nombreProyecto,
                            cliente = nombreCliente,
                            direccion = direccion,
                            estado = estado.ifBlank { "En evaluación" }
                        )
                        viewModel.actualizarProyecto(actualizado)
                        onBack()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar cambios")
            }

            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancelar")
            }
        }
    }
}
