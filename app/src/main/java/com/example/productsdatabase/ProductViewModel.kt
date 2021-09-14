package com.example.productsdatabase

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductViewModel(application: Application) : AndroidViewModel(application) {

    var isDbRoom = true
    private val repository = ProductRepository(ProductDatabase.getDatabase(application).productDao())
    private val dbHelper = ProductDbHelper(application.applicationContext)
    var getAllProducts: LiveData<List<Product>>

    init {
        getAllProducts = if (isDbRoom) {
            repository.getAllProducts("")
        } else {
            dbHelper.getAllProducts("")
        }
    }

    fun sortProducts(order: String) {
        getAllProducts = if (isDbRoom) {
            repository.getAllProducts(order)
        } else {
            dbHelper.getAllProducts(order)
        }
    }

    fun addProduct(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            if (isDbRoom) {
                repository.addProduct(product)
            } else {
                dbHelper.addProduct(product)
            }
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            if (isDbRoom) {
                repository.updateProduct(product)
            } else {
                dbHelper.updateProduct(product)
            }
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            if (isDbRoom) {
                repository.deleteProduct(product)
            } else {
                dbHelper.deleteProduct(product)
            }
        }
    }
}