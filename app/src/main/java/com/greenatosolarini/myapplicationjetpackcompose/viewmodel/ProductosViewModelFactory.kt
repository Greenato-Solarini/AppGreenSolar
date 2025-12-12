package com.greenatosolarini.myapplicationjetpackcompose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.greenatosolarini.myapplicationjetpackcompose.repository.ProductoRepository

class ProductosViewModelFactory(
    private val repository: ProductoRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductosViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductosViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

