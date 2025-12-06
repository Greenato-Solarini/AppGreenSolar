package com.GreenatoSolarini.myapplicationjetpackcompose.viewmodel

import com.GreenatoSolarini.myapplicationjetpackcompose.data.local.FakeClienteDao
import com.GreenatoSolarini.myapplicationjetpackcompose.repository.ClienteRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ClientesViewModelTest {

    private lateinit var viewModel: ClientesViewModel

    @Before
    fun setUp() {
        val fakeDao = FakeClienteDao()
        val fakeRepo = ClienteRepository(fakeDao)
        viewModel = ClientesViewModel(fakeRepo)
    }

    @Test
    fun `nombre vacío no es válido`() {
        assertFalse(viewModel.esNombreValido(""))
    }

    @Test
    fun `nombre con un caracter no es válido`() {
        assertFalse(viewModel.esNombreValido("A"))
    }

    @Test
    fun `nombre válido devuelve true`() {
        assertTrue(viewModel.esNombreValido("Pablo"))
    }
}