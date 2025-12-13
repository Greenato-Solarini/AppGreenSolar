package com.greenatosolarini.myapplicationjetpackcompose.ui.screens.clientes

import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.greenatosolarini.myapplicationjetpackcompose.viewmodel.ClientesViewModel

fun esEmailValido(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun esTelefonoValido(telefono: String): Boolean {
    return telefono.all { it.isDigit() } && telefono.length == 9
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevoClienteScreen(
    viewModel: ClientesViewModel,
    onBack: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var comuna by remember { mutableStateOf("") }

    var nombreError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var telefonoError by remember { mutableStateOf<String?>(null) }
    var comunaError by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuevo cliente") },
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
                label = { Text("Telefono") },
                modifier = Modifier.fillMaxWidth(),
                isError = telefonoError != null
            )
            telefonoError?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            OutlinedTextField(
                value = direccion,
                onValueChange = { direccion = it },
                label = { Text("Direccion") },
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
                        emailError = "Ingresa un email valido."
                        hayError = true
                    }

                    if (!esTelefonoValido(telefono)) {
                        telefonoError = "El telefono debe tener exactamente 9 digitos numericos."
                        hayError = true
                    }


                    if (comuna.isBlank()) {
                        comunaError = "La comuna es obligatoria."
                        hayError = true
                    }

                    if (!hayError) {
                        viewModel.agregarCliente(
                            nombre = nombre,
                            email = email,
                            telefono = telefono,
                            direccion = direccion,
                            comuna = comuna
                        )
                        onBack()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar cliente")
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

