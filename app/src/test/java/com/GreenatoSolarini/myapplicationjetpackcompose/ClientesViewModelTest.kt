package com.greenatosolarini.myapplicationjetpackcompose.viewmodel

import com.greenatosolarini.myapplicationjetpackcompose.data.local.FakeClienteDao
import com.greenatosolarini.myapplicationjetpackcompose.repository.ClienteRepository
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
    fun `nombre vacÃ­o no es vÃ¡lido`() {
        assertFalse(viewModel.esNombreValido(""))
    }

    @Test
    fun `nombre con un caracter no es vÃ¡lido`() {
        assertFalse(viewModel.esNombreValido("A"))
    }

    @Test
    fun `nombre vÃ¡lido devuelve true`() {
        assertTrue(viewModel.esNombreValido("Pablo"))
    }
}
