package com.GreenatoSolarini.myapplicationjetpackcompose.ui.screens.proyectos

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
    var nombre by remember { mutableStateOf(proyecto.nombre) }
    var cliente by remember { mutableStateOf(proyecto.cliente) }
    var direccion by remember { mutableStateOf(proyecto.direccion) }
    var estado by remember { mutableStateOf(proyecto.estado) }
    var error by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar proyecto") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre del Proyecto") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = cliente,
                onValueChange = { cliente = it },
                label = { Text("Cliente") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = direccion,
                onValueChange = { direccion = it },
                label = { Text("Dirección") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = estado,
                onValueChange = { estado = it },
                label = { Text("Estado") },
                modifier = Modifier.fillMaxWidth()
            )

            if (error) {
                Text(
                    "Completa los campos obligatorios",
                    color = MaterialTheme.colorScheme.error
                )
            }

            Button(
                onClick = {
                    if (nombre.isBlank() || cliente.isBlank() || direccion.isBlank()) {
                        error = true
                    } else {
                        val actualizado = proyecto.copy(
                            nombre = nombre,
                            cliente = cliente,
                            direccion = direccion,
                            estado = estado
                        )
                        viewModel.actualizarProyecto(actualizado)
                        onBack()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar cambios")
            }

            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancelar")
            }
        }
    }
}
