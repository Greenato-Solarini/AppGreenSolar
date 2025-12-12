package com.greenatosolarini.myapplicationjetpackcompose.ui.screens.clientes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.greenatosolarini.myapplicationjetpackcompose.model.Cliente
import com.greenatosolarini.myapplicationjetpackcompose.viewmodel.ClientesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarClienteScreen(
    clienteInicial: Cliente,
    viewModel: ClientesViewModel,
    onBack: () -> Unit
) {
    var nombre by remember { mutableStateOf(clienteInicial.nombre) }
    var email by remember { mutableStateOf(clienteInicial.email) }
    var telefono by remember { mutableStateOf(clienteInicial.telefono) }
    var direccion by remember { mutableStateOf(clienteInicial.direccion) }
    var comuna by remember { mutableStateOf(clienteInicial.comuna) }

    var nombreError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var telefonoError by remember { mutableStateOf<String?>(null) }
    var comunaError by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar cliente") },
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

            OutlinedTextField(
                value = nombre,
                onValueChange = {
                    nombre = it
                    nombreError = null
                },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth(),
                isError = nombreError != null
            )
            nombreError?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = null
                },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                isError = emailError != null
            )
            emailError?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            OutlinedTextField(
                value = telefono,
                onValueChange = {
                    telefono = it
                    telefonoError = null
                },
                label = { Text("Teléfono") },
                modifier = Modifier.fillMaxWidth(),
                isError = telefonoError != null
            )
            telefonoError?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            OutlinedTextField(
                value = direccion,
                onValueChange = { direccion = it },
                label = { Text("Dirección") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = comuna,
                onValueChange = {
                    comuna = it
                    comunaError = null
                },
                label = { Text("Comuna") },
                modifier = Modifier.fillMaxWidth(),
                isError = comunaError != null
            )
            comunaError?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Button(
                onClick = {
                    var hayError = false

                    if (nombre.isBlank()) {
                        nombreError = "El nombre es obligatorio."
                        hayError = true
                    }

                    if (!esEmailValido(email)) {
                        emailError = "Ingresa un email válido."
                        hayError = true
                    }

                    if (!esTelefonoValido(telefono)) {
                        telefonoError = "El teléfono debe tener exactamente 9 dígitos numéricos."
                        hayError = true
                    }


                    if (comuna.isBlank()) {
                        comunaError = "La comuna es obligatoria."
                        hayError = true
                    }

                    if (!hayError) {
                        val actualizado = clienteInicial.copy(
                            nombre = nombre,
                            email = email,
                            telefono = telefono,
                            direccion = direccion,
                            comuna = comuna
                        )
                        viewModel.actualizarCliente(actualizado)
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
