package com.greenatosolarini.myapplicationjetpackcompose.ui.screens.proyectos

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.greenatosolarini.myapplicationjetpackcompose.model.Cliente
import com.greenatosolarini.myapplicationjetpackcompose.model.Instalador
import com.greenatosolarini.myapplicationjetpackcompose.model.ProyectoSolar
import com.greenatosolarini.myapplicationjetpackcompose.viewmodel.ClientesViewModel
import com.greenatosolarini.myapplicationjetpackcompose.viewmodel.InstaladoresViewModel
import com.greenatosolarini.myapplicationjetpackcompose.viewmodel.ProyectosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevoProyectoScreen(
    viewModel: ProyectosViewModel,
    clientesViewModel: ClientesViewModel,
    instaladoresViewModel: InstaladoresViewModel,
    onBack: () -> Unit
) {
    val clientes by clientesViewModel.clientes.collectAsState()
    val instaladores by instaladoresViewModel.instaladores.collectAsState()

    var nombreProyecto by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var comuna by remember { mutableStateOf("") }
    var estado by remember { mutableStateOf("En evaluaciÃ³n") }

    var clienteSeleccionado by remember { mutableStateOf<Cliente?>(null) }
    var instaladorSeleccionado by remember { mutableStateOf<Instalador?>(null) }

    var expandedCliente by remember { mutableStateOf(false) }
    var expandedInstalador by remember { mutableStateOf(false) }

    var showError by remember { mutableStateOf(false) }
    var clienteError by remember { mutableStateOf<String?>(null) }
    var instaladorError by remember { mutableStateOf<String?>(null) }

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

            // ---- Nombre proyecto ----
            OutlinedTextField(
                value = nombreProyecto,
                onValueChange = {
                    nombreProyecto = it
                    showError = false
                },
                label = { Text("Nombre del proyecto") },
                modifier = Modifier.fillMaxWidth()
            )

            // ---- CLIENTE (OBLIGATORIO) ----
            if (clientes.isEmpty()) {
                Text(
                    text = "No hay clientes registrados. Primero crea un cliente en el mÃ³dulo Clientes.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            } else {
                Box {
                    OutlinedTextField(
                        value = clienteSeleccionado?.nombre ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Cliente") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expandedCliente = true },   // ðŸ‘ˆ AQUÃ EL CLICK
                        isError = clienteError != null,
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Desplegar lista de clientes"
                            )
                        }
                    )

                    DropdownMenu(
                        expanded = expandedCliente,
                        onDismissRequest = { expandedCliente = false }
                    ) {
                        clientes.forEach { cliente ->
                            DropdownMenuItem(
                                text = { Text(cliente.nombre) },
                                onClick = {
                                    clienteSeleccionado = cliente
                                    clienteError = null
                                    expandedCliente = false
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

            // ---- INSTALADOR (OBLIGATORIO) ----
            if (instaladores.isEmpty()) {
                Text(
                    text = "No hay instaladores registrados. Primero crea un instalador en el mÃ³dulo Instaladores.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            } else {
                Box {
                    OutlinedTextField(
                        value = instaladorSeleccionado?.nombre ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Instalador") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expandedInstalador = true },   // ðŸ‘ˆ AQUÃ TAMBIÃ‰N
                        isError = instaladorError != null,
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Desplegar lista de instaladores"
                            )
                        }
                    )

                    DropdownMenu(
                        expanded = expandedInstalador,
                        onDismissRequest = { expandedInstalador = false }
                    ) {
                        instaladores.forEach { instalador ->
                            DropdownMenuItem(
                                text = { Text(instalador.nombre) },
                                onClick = {
                                    instaladorSeleccionado = instalador
                                    instaladorError = null
                                    expandedInstalador = false
                                }
                            )
                        }
                    }
                }

                instaladorError?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // ---- DirecciÃ³n ----
            OutlinedTextField(
                value = direccion,
                onValueChange = {
                    direccion = it
                    showError = false
                },
                label = { Text("DirecciÃ³n") },
                modifier = Modifier.fillMaxWidth()
            )

            // ---- Comuna ----
            OutlinedTextField(
                value = comuna,
                onValueChange = {
                    comuna = it
                    showError = false
                },
                label = { Text("Comuna") },
                modifier = Modifier.fillMaxWidth()
            )

            // ---- Estado ----
            OutlinedTextField(
                value = estado,
                onValueChange = { estado = it },
                label = { Text("Estado (En evaluaciÃ³n / En instalaciÃ³n / Operativo)") },
                modifier = Modifier.fillMaxWidth()
            )

            if (showError) {
                Text(
                    text = "Completa nombre, cliente, instalador, direcciÃ³n y comuna.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    var hayError = false

                    if (nombreProyecto.isBlank() || direccion.isBlank() || comuna.isBlank()) {
                        showError = true
                        hayError = true
                    }

                    if (clienteSeleccionado == null) {
                        clienteError = "Debes seleccionar un cliente."
                        hayError = true
                    }

                    if (instaladorSeleccionado == null) {
                        instaladorError = "Debes seleccionar un instalador."
                        hayError = true
                    }

                    if (!hayError &&
                        clienteSeleccionado != null &&
                        instaladorSeleccionado != null
                    ) {
                        val nuevoProyecto = ProyectoSolar(
                            id = 0,
                            nombre = nombreProyecto,
                            clienteId = clienteSeleccionado!!.id,
                            direccion = direccion,
                            comuna = comuna,
                            estado = estado.ifBlank { "En evaluaciÃ³n" },
                            produccionActualW = 0,
                            consumoActualW = 0,
                            ahorroHoyClp = 0,
                            foto = null,
                            instaladorId = instaladorSeleccionado!!.id
                        )

                        viewModel.agregarProyecto(nuevoProyecto)
                        onBack()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = clientes.isNotEmpty() && instaladores.isNotEmpty()
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

