package com.greenatosolarini.myapplicationjetpackcompose.ui.screens.productos

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
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.greenatosolarini.myapplicationjetpackcompose.model.Producto
import com.greenatosolarini.myapplicationjetpackcompose.viewmodel.ProductosViewModel

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

    // Foto inicial desde ViewModel / Room
    var fotoProducto by remember {
        mutableStateOf(viewModel.obtenerFotoProducto(producto.id))
    }
    var permisoDenegado by remember { mutableStateOf(false) }

    // CÃ¡mara
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            fotoProducto = bitmap
            viewModel.guardarFotoProducto(producto.id, bitmap)
        }
    }

    // Permiso cÃ¡mara
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

    // GalerÃ­a
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

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar producto solar") },
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
                value = nombre,
                onValueChange = {
                    nombre = it
                    showError = false
                },
                label = { Text("Nombre del producto") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = precioText,
                onValueChange = {
                    precioText = it
                    showError = false
                },
                label = { Text("Precio (CLP)") },
                modifier = Modifier.fillMaxWidth(),
                isError = showError
            )

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("DescripciÃ³n (opcional)") },
                modifier = Modifier.fillMaxWidth()
            )

            if (showError) {
                Text(
                    text = "Revisa el nombre y el precio (debe ser un nÃºmero mayor a 0).",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // BOTONES FOTO
            Button(
                onClick = { manejarClickCamara() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Tomar foto del producto (CÃ¡mara)")
            }

            Button(
                onClick = { manejarClickGaleria() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Elegir foto desde galerÃ­a")
            }

            if (permisoDenegado) {
                Text(
                    text = "Permiso de cÃ¡mara denegado. ActÃ­valo en ajustes para usar esta funciÃ³n.",
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
                    contentDescription = "Foto del producto",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

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

            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancelar")
            }
        }
    }
}

