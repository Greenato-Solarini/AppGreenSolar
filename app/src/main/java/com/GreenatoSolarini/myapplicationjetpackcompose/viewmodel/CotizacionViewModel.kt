package com.GreenatoSolarini.myapplicationjetpackcompose.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlin.math.ceil

data class CotizacionResultado(
    val potenciaRecomendadaKw: Double,
    val numeroPaneles: Int,
    val inversionAproximada: Int,
    val ahorroMensualEstimado: Int
)

class CotizacionViewModel : ViewModel() {

    // Campos del formulario
    var nombreCliente by mutableStateOf("")
    var tipoPropiedad by mutableStateOf("Casa")
    var consumoMensualText by mutableStateOf("")  // kWh/mes
    var metrosTechoText by mutableStateOf("")     // m2
    var incluirBaterias by mutableStateOf(false)

    // Errores
    var nombreError by mutableStateOf<String?>(null)
    var consumoError by mutableStateOf<String?>(null)
    var metrosTechoError by mutableStateOf<String?>(null)

    // Resultado
    var resultado by mutableStateOf<CotizacionResultado?>(null)
        private set

    fun onCalcular() {
        limpiarErrores()
        resultado = null

        var hayError = false

        if (nombreCliente.isBlank()) {
            nombreError = "El nombre del cliente es obligatorio"
            hayError = true
        }

        val consumo = consumoMensualText.toDoubleOrNull()
        if (consumo == null || consumo <= 0) {
            consumoError = "Ingresa un consumo mensual válido (en kWh)"
            hayError = true
        }

        val metros = metrosTechoText.toDoubleOrNull()
        if (metros == null || metros <= 0) {
            metrosTechoError = "Ingresa los metros cuadrados del techo"
            hayError = true
        }

        if (hayError || consumo == null || metros == null) {
            return
        }

        // --- Lógica simple de cálculo (puedes explicarla en la defensa) ---

        // Asumimos un factor de producción: 1 kW instalado ≈ 140 kWh/mes (depende zona)
        val kwNecesarios = consumo / 140.0

        // Paneles de 550W (~0.55kW)
        val paneles = ceil(kwNecesarios / 0.55).toInt().coerceAtLeast(1)

        // Costo estimado por panel
        val costoPorPanel = 185_000
        var inversion = paneles * costoPorPanel + 500_000  // inversor + instalación base

        if (incluirBaterias) {
            inversion += 1_500_000
        }

        // Ahorro mensual simple: 70% de la cuenta (muy aproximado)
        val ahorroEstimado = (consumo * 150.0 * 0.7).toInt() // 150 CLP por kWh aprox.

        resultado = CotizacionResultado(
            potenciaRecomendadaKw = (paneles * 0.55),
            numeroPaneles = paneles,
            inversionAproximada = inversion,
            ahorroMensualEstimado = ahorroEstimado
        )
    }

    private fun limpiarErrores() {
        nombreError = null
        consumoError = null
        metrosTechoError = null
    }
}
