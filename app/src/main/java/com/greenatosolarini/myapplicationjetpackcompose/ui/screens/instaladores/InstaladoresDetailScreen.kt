package com.greenatosolarini.myapplicationjetpackcompose.ui.screens.instaladores

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.greenatosolarini.myapplicationjetpackcompose.model.Instalador
import com.greenatosolarini.myapplicationjetpackcompose.model.ProyectoSolar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InstaladorDetailScreen(
    instalador: Instalador,
    proyectosAsignados: List<ProyectoSolar>,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(instalador.nombre) },
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Datos del instalador
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White,
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("Nombre: ${instalador.nombre}", style = MaterialTheme.typography.titleMedium)
                    if (instalador.telefono.isNotBlank()) {
                        Text("TelÃ©fono: ${instalador.telefono}")
                    }
                    if (instalador.email.isNotBlank()) {
                        Text("Email: ${instalador.email}")
                    }
                }
            }

            Text(
                text = "Proyectos asignados",
                style = MaterialTheme.typography.titleMedium
            )

            if (proyectosAsignados.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Este instalador no tiene proyectos asignados.")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(proyectosAsignados) { proyecto ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Text(proyecto.nombre, style = MaterialTheme.typography.titleMedium)
                                Text("DirecciÃ³n: ${proyecto.direccion}")
                                Text("Comuna: ${proyecto.comuna}")
                                Text("Estado: ${proyecto.estado}")
                            }
                        }
                    }
                }
            }
        }
    }
}

