package com.GreenatoSolarini.myapplicationjetpackcompose.ui.screens.proyectos

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.GreenatoSolarini.myapplicationjetpackcompose.model.Cliente
import com.GreenatoSolarini.myapplicationjetpackcompose.model.ProyectoSolar
import com.GreenatoSolarini.myapplicationjetpackcompose.viewmodel.ClientesViewModel
import com.GreenatoSolarini.myapplicationjetpackcompose.viewmodel.ProyectosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevoProyectoScreen(
    proyectosViewModel: ProyectosViewModel,
    clientesViewModel: ClientesViewModel,
    onBack: () -> Unit
) {
    var nombreProyecto by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var estado by remember { mutableStateOf("En evaluación") }

    // 🔹 Clientes disponibles
    val clientes by clientesViewModel.clientes.collectAsState()

    // 🔹 Cliente seleccionado
    var clienteSeleccionado by remember { mutableStateOf<Cliente?>(null) }
    var expanded by remember { mutableStateOf(false) }

    var showError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuevo proyecto solar") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
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

            // 🔥 Si no hay clientes no puede crear proyectos
            if (clientes.isEmpty()) {
                Text(
                    "Primero debes registrar clientes antes de crear proyectos.",
                    color = MaterialTheme.colorScheme.error
                )

                Button(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Volver")
                }
                return@Column
            }

            OutlinedTextField(
                value = nombreProyecto,
                onValueChange = {
                    nombreProyecto = it
                    showError = false
                },
                label = { Text("Nombre del proyecto") },
                modifier = Modifier.fillMaxWidth()
            )

            // -------------------------------
            //         DROPDOWN CLIENTES
            // -------------------------------
            Column {
                Text("Cliente", style = MaterialTheme.typography.bodyMedium)

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expanded = true }
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = clienteSeleccionado?.nombre ?: "Seleccionar cliente...",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    clientes.forEach { cliente ->
                        DropdownMenuItem(
                            text = { Text(cliente.nombre) },
                            onClick = {
                                clienteSeleccionado = cliente
                                expanded = false
                                showError = false
                            }
                        )
                    }
                }
            }

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
                    text = "Completa todos los campos y selecciona un cliente.",
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    if (
                        nombreProyecto.isBlank() ||
                        direccion.isBlank() ||
                        clienteSeleccionado == null
                    ) {
                        showError = true
                        return@Button
                    }

                    // Crear el proyecto con tu estructura correcta
                    val nuevoProyecto = ProyectoSolar(
                        id = 0,
                        nombre = nombreProyecto,
                        cliente = clienteSeleccionado!!.nombre,
                        direccion = direccion,
                        estado = estado.ifBlank { "En evaluación" },
                        produccionActualW = 0,
                        consumoActualW = 0,
                        ahorroHoyClp = 0,
                        foto = null // foto se agregará luego en editar
                    )

                    proyectosViewModel.agregarProyecto(nuevoProyecto)
                    onBack()
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
