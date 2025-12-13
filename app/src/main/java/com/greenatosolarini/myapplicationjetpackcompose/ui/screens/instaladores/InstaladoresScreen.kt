package com.greenatosolarini.myapplicationjetpackcompose.ui.screens.instaladores

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.greenatosolarini.myapplicationjetpackcompose.model.Instalador
import com.greenatosolarini.myapplicationjetpackcompose.viewmodel.InstaladoresViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InstaladoresScreen(
    viewModel: InstaladoresViewModel,
    onNavigateToNuevo: () -> Unit,
    onInstaladorClick: (Int) -> Unit,
    onInstaladorEdit: (Int) -> Unit,
    onBack: () -> Unit
) {
    val instaladores by viewModel.instaladores.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Instaladores") },
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
                    contentDescription = "Nuevo instalador"
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            if (instaladores.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay instaladores registrados todavia.")
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    items(instaladores) { instalador ->
                        InstaladorItem(
                            instalador = instalador,
                            onClick = { onInstaladorClick(instalador.id) },
                            onEdit = { onInstaladorEdit(instalador.id) },
                            onDelete = { viewModel.eliminarInstalador(instalador) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InstaladorItem(
    instalador: Instalador,
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
                text = instalador.nombre,
                style = MaterialTheme.typography.titleMedium
            )
            Text(text = "Email: ${instalador.email}")
            Text(text = "TelÃ©fono: ${instalador.telefono}")

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onEdit) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar instalador"
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar instalador"
                    )
                }
            }
        }
    }
}

