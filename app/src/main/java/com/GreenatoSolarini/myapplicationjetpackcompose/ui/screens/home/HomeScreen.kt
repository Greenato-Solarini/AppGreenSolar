package com.GreenatoSolarini.myapplicationjetpackcompose.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.GreenatoSolarini.myapplicationjetpackcompose.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToProductos: () -> Unit,
    onNavigateToCotizacion: () -> Unit,
    onNavigateToProyectos: () -> Unit,
    onNavigateToClientes: () -> Unit,
    onNavigateToInstaladores: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("GreenSolar App") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // ---------- LOGO (más compacto) ----------
            Image(
                painter = painterResource(id = R.drawable.greenatosolarini),
                contentDescription = "Logo GreenSolar",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(120.dp)
                    .padding(top = 8.dp, bottom = 8.dp)
            )

            HomeOptionCard(
                title = "Clientes",
                icon = Icons.Default.Person,
                onClick = onNavigateToClientes
            )

            HomeOptionCard(
                title = "Instaladores",
                icon = Icons.Default.Engineering,
                onClick = onNavigateToInstaladores
            )

            HomeOptionCard(
                title = "Proyectos solares",
                icon = Icons.Default.Home,
                onClick = onNavigateToProyectos
            )

            HomeOptionCard(
                title = "Productos solares",
                icon = Icons.Default.Build,
                onClick = onNavigateToProductos
            )

            HomeOptionCard(
                title = "Cotización",
                icon = Icons.Default.Star,
                onClick = onNavigateToCotizacion
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeOptionCard(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(68.dp), // 👈 más bajo que antes
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
