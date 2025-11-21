package com.GreenatoSolarini.myapplicationjetpackcompose.ui.screens.cotizaciones

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.GreenatoSolarini.myapplicationjetpackcompose.viewmodel.CotizacionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CotizacionScreen(
    viewModel: CotizacionViewModel,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Calculadora Solar") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
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

            // ===========================
            // 🔵 Consumo mensual
            // ===========================
            OutlinedTextField(
                value = viewModel.consumoMensualText,
                onValueChange = {
                    viewModel.consumoMensualText = it
                    viewModel.consumoError = null
                },
                label = { Text("Consumo mensual (kWh)") },
                modifier = Modifier.fillMaxWidth(),
                isError = viewModel.consumoError != null
            )
            if (viewModel.consumoError != null) {
                Text(
                    text = viewModel.consumoError ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // ===========================
            // 🔵 Metros de techo
            // ===========================
            OutlinedTextField(
                value = viewModel.metrosTechoText,
                onValueChange = {
                    viewModel.metrosTechoText = it
                    viewModel.metrosTechoError = null
                },
                label = { Text("Superficie disponible en techo (m²)") },
                modifier = Modifier.fillMaxWidth(),
                isError = viewModel.metrosTechoError != null
            )
            if (viewModel.metrosTechoError != null) {
                Text(
                    text = viewModel.metrosTechoError ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // ===========================
            // 🔵 Checkbox baterías
            // ===========================
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                Checkbox(
                    checked = viewModel.incluirBaterias,
                    onCheckedChange = { viewModel.incluirBaterias = it }
                )
                Text("Incluir baterías de respaldo")
            }

            // ===========================
            // 🔵 Botón calcular
            // ===========================
            Button(
                onClick = { viewModel.onCalcular() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Calcular")
            }

            // ===========================
            // 🟢 Resultado
            // ===========================
            viewModel.resultado?.let { resultado ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text("Resultado", style = MaterialTheme.typography.titleMedium)
                        Text("Potencia recomendada: ${"%.1f".format(resultado.potenciaRecomendadaKw)} kW")
                        Text("Número de paneles: ${resultado.numeroPaneles}")
                        Text("Inversión estimada: ${resultado.inversionAproximada} CLP")
                        Text("Ahorro mensual estimado: ${resultado.ahorroMensualEstimado} CLP")
                    }
                }
            }
        }
    }
}
