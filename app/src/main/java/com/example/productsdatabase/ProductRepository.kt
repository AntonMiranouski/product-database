package com.example.productsdatabase

import androidx.lifecycle.LiveData

class ProductRepository(private val productDao: ProductDao) {

    fun getAllProducts(order: String): LiveData<List<Product>> = productDao.getAllProducts(order)

    suspend fun addProduct(product: Product) {
        productDao.addProduct(product)
    }

    suspend fun updateProduct(product: Product) {
        productDao.updateProduct(product)
    }

    suspend fun deleteProduct(product: Product) {
        productDao.deleteProduct(product)
    }
}