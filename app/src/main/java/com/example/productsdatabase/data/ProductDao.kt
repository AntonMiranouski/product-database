package com.example.productsdatabase.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.productsdatabase.Product

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addProduct(product: Product)

    @Update
    suspend fun updateProduct(product: Product)

    @Delete
    suspend fun deleteProduct(product: Product)

    @Query(
        "SELECT * FROM product_table ORDER BY " +
                "CASE WHEN :order = 'name' THEN name END ASC, " +
                "CASE WHEN :order = 'price' THEN price END ASC, " +
                "CASE WHEN :order = '' THEN id END DESC "
    )
    fun getAllProducts(order: String): LiveData<List<Product>>
}