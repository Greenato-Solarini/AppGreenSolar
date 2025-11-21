package com.GreenatoSolarini.myapplicationjetpackcompose.ui.screens.proyectos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.unit.dp
import com.GreenatoSolarini.myapplicationjetpackcompose.model.ProyectoSolar
import com.GreenatoSolarini.myapplicationjetpackcompose.viewmodel.ProyectosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarProyectoScreen(
    proyecto: ProyectoSolar,
    viewModel: ProyectosViewModel,
    onBack: () -> Unit
) {
    var nombreProyecto by remember { mutableStateOf(proyecto.nombre) }
    var direccion by remember { mutableStateOf(proyecto.direccion) }
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
                label = { Text("Dirección / Comuna") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = estado,
                onValueChange = { estado = it },
                label = { Text("Estado (En evaluación / En instalación / Operativo)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = produccionText,
                onValueChange = { produccionText = it },
                label = { Text("Producción actual (W)") },
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
                    text = "Nombre y dirección no pueden estar vacíos.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (nombreProyecto.isBlank() || direccion.isBlank()) {
                        showError = true
                    } else {
                        val produccion = produccionText.toIntOrNull() ?: 0
                        val consumo = consumoText.toIntOrNull() ?: 0
                        val ahorro = ahorroText.toIntOrNull() ?: 0

                        val proyectoActualizado = proyecto.copy(
                            nombre = nombreProyecto,
                            direccion = direccion,
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
