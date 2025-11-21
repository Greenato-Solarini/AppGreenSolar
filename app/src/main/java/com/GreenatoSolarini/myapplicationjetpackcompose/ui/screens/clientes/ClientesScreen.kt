package com.GreenatoSolarini.myapplicationjetpackcompose.ui.screens.clientes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.GreenatoSolarini.myapplicationjetpackcompose.model.Cliente
import com.GreenatoSolarini.myapplicationjetpackcompose.viewmodel.ClientesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientesScreen(
    viewModel: ClientesViewModel,
    onNavigateToNuevo: () -> Unit,
    onClienteClick: (Int) -> Unit,
    onClienteEdit: (Int) -> Unit,
    onBack: () -> Unit
) {
    val clientes by viewModel.clientes.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Clientes") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToNuevo,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Nuevo cliente"
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            if (clientes.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay clientes registrados todavía.")
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    items(clientes) { cliente ->
                        ClienteItem(
                            cliente = cliente,
                            onClick = { onClienteClick(cliente.id) },
                            onEdit = { onClienteEdit(cliente.id) },
                            onDelete = { viewModel.eliminarCliente(cliente) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ClienteItem(
    cliente: Cliente,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = cliente.nombre,
                style = MaterialTheme.typography.titleMedium
            )
            Text(text = "Email: ${cliente.email}")
            Text(text = "Teléfono: ${cliente.telefono}")
            Text(text = "Dirección: ${cliente.direccion}")
            Text(text = "Comuna: ${cliente.comuna}")

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onEdit) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar cliente"
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar cliente"
                    )
                }
            }
        }
    }
}
