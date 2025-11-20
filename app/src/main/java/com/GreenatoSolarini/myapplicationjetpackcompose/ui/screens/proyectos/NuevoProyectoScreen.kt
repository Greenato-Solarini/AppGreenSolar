package com.GreenatoSolarini.myapplicationjetpackcompose.ui.screens.proyectos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.GreenatoSolarini.myapplicationjetpackcompose.viewmodel.ProyectosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevoProyectoScreen(
    viewModel: ProyectosViewModel,
    onBack: () -> Unit
) {
    var nombreProyecto by remember { mutableStateOf("") }
    var nombreCliente by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var estado by remember { mutableStateOf("En evaluación") }

    var showError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuevo proyecto solar") }
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

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (nombreProyecto.isBlank() ||
                        nombreCliente.isBlank() ||
                        direccion.isBlank()
                    ) {
                        showError = true
                    } else {
                        viewModel.crearProyecto(
                            nombre = nombreProyecto,
                            cliente = nombreCliente,
                            direccion = direccion,
                            estado = estado.ifBlank { "En evaluación" }
                        )
                        onBack()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar proyecto")
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
