package com.GreenatoSolarini.myapplicationjetpackcompose.ui.screens.proyectos

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
    viewModel: ProyectosViewModel,
    clientesViewModel: ClientesViewModel,
    onBack: () -> Unit
) {
    val clientes by clientesViewModel.clientes.collectAsState()

    var nombreProyecto by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var estado by remember { mutableStateOf("En evaluación") }

    var clienteSeleccionado by remember { mutableStateOf<Cliente?>(null) }
    var expanded by remember { mutableStateOf(false) }

    var showError by remember { mutableStateOf(false) }
    var clienteError by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuevo proyecto solar") },
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

            // -------- SELECCIÓN DE CLIENTE --------
            if (clientes.isEmpty()) {
                Text(
                    text = "No hay clientes registrados. Primero crea un cliente en el módulo Clientes.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            } else {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = clienteSeleccionado?.nombre ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Cliente") },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        isError = clienteError != null
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        clientes.forEach { cliente ->
                            DropdownMenuItem(
                                text = { Text(cliente.nombre) },
                                onClick = {
                                    clienteSeleccionado = cliente
                                    clienteError = null
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                clienteError?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
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
                    text = "Completa nombre, cliente y dirección.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    var hayError = false

                    if (nombreProyecto.isBlank() || direccion.isBlank()) {
                        showError = true
                        hayError = true
                    }

                    if (clienteSeleccionado == null) {
                        clienteError = "Debes seleccionar un cliente."
                        hayError = true
                    }

                    if (!hayError && clienteSeleccionado != null) {
                        val nuevoProyecto = ProyectoSolar(
                            id = 0,
                            nombre = nombreProyecto,
                            clienteId = clienteSeleccionado!!.id,
                            direccion = direccion,
                            estado = estado.ifBlank { "En evaluación" },
                            produccionActualW = 0,
                            consumoActualW = 0,
                            ahorroHoyClp = 0,
                            foto = null
                        )

                        viewModel.agregarProyecto(nuevoProyecto)
                        onBack()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = clientes.isNotEmpty()
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
