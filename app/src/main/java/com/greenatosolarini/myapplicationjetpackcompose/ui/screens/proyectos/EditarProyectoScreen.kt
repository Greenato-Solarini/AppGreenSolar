package com.greenatosolarini.myapplicationjetpackcompose.ui.screens.proyectos

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.greenatosolarini.myapplicationjetpackcompose.model.ProyectoSolar
import com.greenatosolarini.myapplicationjetpackcompose.viewmodel.ProyectosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarProyectoScreen(
    proyecto: ProyectoSolar,
    viewModel: ProyectosViewModel,
    onBack: () -> Unit
) {
    var nombreProyecto by remember { mutableStateOf(proyecto.nombre) }
    var direccion by remember { mutableStateOf(proyecto.direccion) }
    var comuna by remember { mutableStateOf(proyecto.comuna) }   // ðŸ‘ˆ NUEVO
    var estado by remember { mutableStateOf(proyecto.estado) }

    var produccionText by remember { mutableStateOf(proyecto.produccionActualW.toString()) }
    var consumoText by remember { mutableStateOf(proyecto.consumoActualW.toString()) }
    var ahorroText by remember { mutableStateOf(proyecto.ahorroHoyClp.toString()) }

    var showError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar proyecto solar") },
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
                value = direccion,
                onValueChange = {
                    direccion = it
                    showError = false
                },
                label = { Text("Direccion") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = comuna,
                onValueChange = {
                    comuna = it
                    showError = false
                },
                label = { Text("Comuna") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = estado,
                onValueChange = { estado = it },
                label = { Text("Estado (En evaluacion / En instalacion / Operativo)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = produccionText,
                onValueChange = { produccionText = it },
                label = { Text("Produccion actual (W)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = consumoText,
                onValueChange = { consumoText = it },
                label = { Text("Consumo actual (W)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = ahorroText,
                onValueChange = { ahorroText = it },
                label = { Text("Ahorro estimado hoy (CLP)") },
                modifier = Modifier.fillMaxWidth()
            )

            if (showError) {
                Text(
                    text = "Nombre, direccion y comuna no pueden estar vacios.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (nombreProyecto.isBlank() || direccion.isBlank() || comuna.isBlank()) {
                        showError = true
                    } else {
                        val produccion = produccionText.toIntOrNull() ?: 0
                        val consumo = consumoText.toIntOrNull() ?: 0
                        val ahorro = ahorroText.toIntOrNull() ?: 0

                        val proyectoActualizado = proyecto.copy(
                            nombre = nombreProyecto,
                            direccion = direccion,
                            comuna = comuna,                       // ðŸ‘ˆ NUEVO
                            estado = estado.ifBlank { proyecto.estado },
                            produccionActualW = produccion,
                            consumoActualW = consumo,
                            ahorroHoyClp = ahorro
                        )

                        viewModel.actualizarProyecto(proyectoActualizado)
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

