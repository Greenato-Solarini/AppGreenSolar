package com.GreenatoSolarini.myapplicationjetpackcompose.ui.screens.cotizaciones

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Checkbox
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
                title = { Text("Cotización GreenSolar") }
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

            // Nombre cliente
            OutlinedTextField(
                value = viewModel.nombreCliente,
                onValueChange = {
                    viewModel.nombreCliente = it
                    viewModel.nombreError = null
                },
                label = { Text("Nombre del cliente") },
                modifier = Modifier.fillMaxWidth(),
                isError = viewModel.nombreError != null
            )
            if (viewModel.nombreError != null) {
                Text(
                    text = viewModel.nombreError ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Tipo de propiedad
            OutlinedTextField(
                value = viewModel.tipoPropiedad,
                onValueChange = { viewModel.tipoPropiedad = it },
                label = { Text("Tipo de propiedad (Casa / PYME / Campo)") },
                modifier = Modifier.fillMaxWidth()
            )

            // Consumo mensual
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

            // Metros de techo
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

            // Checkbox baterías
            Row {
                Checkbox(
                    checked = viewModel.incluirBaterias,
                    onCheckedChange = { viewModel.incluirBaterias = it }
                )
                Text("Incluir baterías de respaldo")
            }

            Button(
                onClick = { viewModel.onCalcular() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Calcular cotización")
            }

            Spacer(modifier = Modifier.height(8.dp))

            val resultado = viewModel.resultado
            if (resultado != null) {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Resultado de la cotización",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text("Potencia recomendada: ${"%.1f".format(resultado.potenciaRecomendadaKw)} kW")
                        Text("Número de paneles: ${resultado.numeroPaneles}")
                        Text("Inversión estimada: ${resultado.inversionAproximada} CLP")
                        Text("Ahorro mensual estimado: ${resultado.ahorroMensualEstimado} CLP")
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver")
            }
        }
    }
}
