package com.example.productsdatabase

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductViewModel(application: Application, private val position: Int) :
    AndroidViewModel(application) {

    private val repository =
        ProductRepository(ProductDatabase.getDatabase(application.applicationContext).productDao())
    private val dbHelper = ProductDbHelper(application.applicationContext)
    var getAllProducts: LiveData<List<Product>>

    // position == 0 for Room implementation, 1 for Cursor
    init {
        getAllProducts = if (position == 0) {
            repository.getAllProducts("")
        } else {
            dbHelper.getAllProducts("")
        }
    }

    fun sortProducts(order: String) {
        getAllProducts = if (position == 0) {
            repository.getAllProducts(order)
        } else {
            dbHelper.getAllProducts(order)
        }
    }

    fun addProduct(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            if (position == 0) {
                repository.addProduct(product)
            } else {
                dbHelper.addProduct(product)
            }
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            if (position == 0) {
                repository.updateProduct(product)
            } else {
                dbHelper.updateProduct(product)
            }
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            if (position == 0) {
                repository.deleteProduct(product)
            } else {
                dbHelper.deleteProduct(product)
            }
        }
    }
}