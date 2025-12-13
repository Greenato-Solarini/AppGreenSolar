package com.greenatosolarini.myapplicationjetpackcompose.ui.screens.clientes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.greenatosolarini.myapplicationjetpackcompose.model.Cliente

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClienteDetailScreen(
    cliente: Cliente,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(cliente.nombre) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
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

            Text(
                text = "Nombre: ${cliente.nombre}",
                style = MaterialTheme.typography.titleMedium
            )

            if (cliente.email.isNotBlank()) {
                Text(
                    text = "Email: ${cliente.email}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (cliente.telefono.isNotBlank()) {
                Text(
                    text = "Telefono: ${cliente.telefono}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (cliente.direccion.isNotBlank()) {
                Text(
                    text = "Direccion: ${cliente.direccion}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (
                cliente.email.isBlank() &&
                cliente.telefono.isBlank() &&
                cliente.direccion.isBlank()
            ) {
                Text(
                    text = "Este cliente no tiene mas datos registrados.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

