package com.GreenatoSolarini.myapplicationjetpackcompose.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToProductos: () -> Unit,
    onNavigateToCotizacion: () -> Unit,
    onNavigateToProyectos: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("GreenSolar SPA") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Panel principal",
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = "Gestiona productos, proyectos solares y cotizaciones.",
                style = MaterialTheme.typography.bodyMedium
            )

            Button(onClick = onNavigateToProductos) {
                Text("Ver productos solares")
            }

            Button(onClick = onNavigateToCotizacion) {
                Text("Calcular cotización solar")
            }

            Button(onClick = onNavigateToProyectos) {
                Text("Ver proyectos solares")
            }
        }
    }
}
