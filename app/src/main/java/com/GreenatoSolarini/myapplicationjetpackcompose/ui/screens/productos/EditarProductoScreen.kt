package com.GreenatoSolarini.myapplicationjetpackcompose.ui.screens.productos

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.GreenatoSolarini.myapplicationjetpackcompose.model.Producto
import com.GreenatoSolarini.myapplicationjetpackcompose.viewmodel.ProductosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarProductoScreen(
    producto: Producto,
    viewModel: ProductosViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    var nombre by remember { mutableStateOf(producto.nombre) }
    var precioText by remember { mutableStateOf(producto.precio.toString()) }
    var descripcion by remember { mutableStateOf(producto.descripcion) }
    var showError by remember { mutableStateOf(false) }

    var fotoProducto by remember {
        mutableStateOf<Bitmap?>(viewModel.obtenerFotoProducto(producto.id))
    }
    var permisoDenegado by remember { mutableStateOf(false) }

    // Launcher para cámara
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            fotoProducto = bitmap
            viewModel.guardarFotoProducto(producto.id, bitmap)
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

    // Launcher para elegir imagen desde galería
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
                fotoProducto = it
                viewModel.guardarFotoProducto(producto.id, it)
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
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Botón GUARDAR
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

            // Recurso nativo 1: CÁMARA
            Button(
                onClick = { manejarClickCamara() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Tomar foto del producto (Cámara)")
            }

            // Recurso nativo 2: GALERÍA
            OutlinedButton(
                onClick = { manejarClickGaleria() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Elegir imagen desde galería")
            }

            if (permisoDenegado) {
                Text(
                    text = "Permiso de cámara denegado. Actívalo en ajustes para usar esta función.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            fotoProducto?.let { bitmap ->
                Text(
                    text = "Imagen asociada al producto:",
                    style = MaterialTheme.typography.bodyMedium
                )
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Imagen del producto",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                )
            }

            // Botón CANCELAR al final
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancelar")
            }
        }
    }
}
