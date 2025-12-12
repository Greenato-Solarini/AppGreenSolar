package com.greenatosolarini.myapplicationjetpackcompose.ui.screens.proyectos

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
import com.greenatosolarini.myapplicationjetpackcompose.model.ProyectoSolar
import com.greenatosolarini.myapplicationjetpackcompose.viewmodel.ClientesViewModel
import com.greenatosolarini.myapplicationjetpackcompose.viewmodel.ProyectosViewModel
import com.greenatosolarini.myapplicationjetpackcompose.viewmodel.InstaladoresViewModel   // ðŸ‘ˆ IMPORT IMPORTANTE

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProyectosScreen(
    viewModel: ProyectosViewModel,
    clientesViewModel: ClientesViewModel,
    instaladoresViewModel: InstaladoresViewModel,   // ðŸ‘ˆ NUEVO
    onProyectoClick: (Int) -> Unit,
    onProyectoEdit: (Int) -> Unit,
    onProyectoDelete: (Int) -> Unit,
    onNavigateToNuevo: () -> Unit,
    onBack: () -> Unit
) {
    val proyectos by viewModel.proyectos.collectAsState()
    val clientes by clientesViewModel.clientes.collectAsState()
    val instaladores by instaladoresViewModel.instaladores.collectAsState()  // ðŸ‘ˆ AQUÃ VA LA LÃNEA CORRECTA

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Proyectos solares") },
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
                    contentDescription = "Nuevo proyecto"
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            if (proyectos.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay proyectos registrados todavÃ­a.")
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    items(proyectos) { proyecto ->
                        val clienteNombre = clientes
                            .firstOrNull { it.id == proyecto.clienteId }
                            ?.nombre ?: "Cliente no encontrado"

                        val instaladorNombre = proyecto.instaladorId?.let { idInst ->
                            instaladores.firstOrNull { it.id == idInst }?.nombre
                        } ?: "Sin instalador asignado"

                        ProyectoItem(
                            proyecto = proyecto,
                            clienteNombre = clienteNombre,
                            instaladorNombre = instaladorNombre,   // ðŸ‘ˆ PASAMOS NOMBRE
                            onClick = { onProyectoClick(proyecto.id) },
                            onEdit = { onProyectoEdit(proyecto.id) },
                            onDelete = { onProyectoDelete(proyecto.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProyectoItem(
    proyecto: ProyectoSolar,
    clienteNombre: String,
    instaladorNombre: String,          // ðŸ‘ˆ NUEVO
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
            Text(text = proyecto.nombre, style = MaterialTheme.typography.titleMedium)
            Text(text = "Cliente: $clienteNombre")
            Text(text = "Instalador: $instaladorNombre")
            Text(text = "DirecciÃ³n: ${proyecto.direccion}")
            Text(text = "Comuna: ${proyecto.comuna}")
            Text(text = "Estado: ${proyecto.estado}")

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onEdit) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar proyecto"
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar proyecto"
                    )
                }
            }
        }
    }
}

