package com.greenatosolarini.myapplicationjetpackcompose.ui.screens.instaladores

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
import com.greenatosolarini.myapplicationjetpackcompose.model.Instalador
import com.greenatosolarini.myapplicationjetpackcompose.viewmodel.InstaladoresViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarInstaladorScreen(
    instaladorInicial: Instalador,
    viewModel: InstaladoresViewModel,
    onBack: () -> Unit
) {
    var nombre by remember { mutableStateOf(instaladorInicial.nombre) }
    var telefono by remember { mutableStateOf(instaladorInicial.telefono) }
    var email by remember { mutableStateOf(instaladorInicial.email) }

    var nombreError by remember { mutableStateOf<String?>(null) }
    var telefonoError by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar instalador") },
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
                value = nombre,
                onValueChange = {
                    nombre = it
                    nombreError = null
                },
                label = { Text("Nombre del instalador") },
                modifier = Modifier.fillMaxWidth(),
                isError = nombreError != null
            )
            nombreError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            OutlinedTextField(
                value = telefono,
                onValueChange = {
                    if (it.all { c -> c.isDigit() }) {
                        telefono = it
                        telefonoError = null
                    }
                },
                label = { Text("Telefono (9 digitos)") },
                modifier = Modifier.fillMaxWidth(),
                isError = telefonoError != null
            )
            telefonoError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email (opcional)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    var hayError = false

                    if (nombre.isBlank()) {
                        nombreError = "El nombre no puede estar vacio."
                        hayError = true
                    }

                    if (telefono.length != 9) {
                        telefonoError = "El telefono debe tener 9 digitos."
                        hayError = true
                    }

                    if (!hayError) {
                        val actualizado = instaladorInicial.copy(
                            nombre = nombre,
                            telefono = telefono,
                            email = email
                        )
                        viewModel.actualizarInstalador(actualizado)
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

