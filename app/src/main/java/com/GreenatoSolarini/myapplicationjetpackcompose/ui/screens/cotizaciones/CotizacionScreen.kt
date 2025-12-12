package com.greenatosolarini.myapplicationjetpackcompose.ui.screens.cotizaciones

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.greenatosolarini.myapplicationjetpackcompose.viewmodel.CotizacionViewModel

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
            // ðŸ”µ Consumo mensual
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
            // ðŸ”µ Metros de techo
            // ===========================
            OutlinedTextField(
                value = viewModel.metrosTechoText,
                onValueChange = {
                    viewModel.metrosTechoText = it
                    viewModel.metrosTechoError = null
                },
                label = { Text("Superficie disponible en techo (mÂ²)") },
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
            // ðŸ”µ Checkbox baterÃ­as
            // ===========================
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                Checkbox(
                    checked = viewModel.incluirBaterias,
                    onCheckedChange = { viewModel.incluirBaterias = it }
                )
                Text("Incluir baterÃ­as de respaldo")
            }

            // ===========================
            // ðŸ”µ BotÃ³n calcular
            // ===========================
            Button(
                onClick = { viewModel.onCalcular() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Calcular")
            }

            // ===========================
            // ðŸŸ¢ Resultado
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
                        Text("NÃºmero de paneles: ${resultado.numeroPaneles}")
                        Text("InversiÃ³n estimada: ${resultado.inversionAproximada} CLP")
                        Text("Ahorro mensual estimado: ${resultado.ahorroMensualEstimado} CLP")
                    }
                }
            }
        }
    }
}

