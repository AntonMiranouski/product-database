package com.example.productsdatabase

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.sql.SQLException

private const val LOG_TAG = "ProductDbHelper"
private const val DATABASE_NAME = "product_database"
private const val TABLE_NAME = "product_table"
private const val DATABASE_VERSION = 1
private const val CREATE_TABLE_SQL =
    "CREATE TABLE IF NOT EXISTS $TABLE_NAME (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, price REAL NOT NULL);"


class ProductDbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION), ProductDao {

    override fun onCreate(db: SQLiteDatabase) {
        try {
            db.execSQL(CREATE_TABLE_SQL)
        } catch (exception: SQLException) {
            Log.e(LOG_TAG, "Exception while trying to create database", exception)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.d(LOG_TAG, "onUpgrade called")
    }

    private fun getCursor(query: String/*SupportSQLiteQuery*/): Cursor {
        return readableDatabase.rawQuery(query, null)
    }

    override suspend fun addProduct(product: Product) {
        val values = ContentValues().apply {
            put("name", product.name)
            put("price", product.price)
        }
        readableDatabase.insert(TABLE_NAME, null, values)
    }

    override suspend fun updateProduct(product: Product) {
        val values = ContentValues().apply {
            put("name", product.name)
            put("price", product.price)
        }
        val selection = "id = ?"
        val selectionArgs = arrayOf(product.id.toString())
        readableDatabase.update(TABLE_NAME, values, selection, selectionArgs)
    }

    override suspend fun deleteProduct(product: Product) {
        val selection = "id = ?"
        val selectionArgs = arrayOf(product.id.toString())
        readableDatabase.delete(TABLE_NAME, selection, selectionArgs)
    }

    override fun getAllProducts(order: String): LiveData<List<Product>> {
        val products = mutableListOf<Product>()
        var query = "SELECT * FROM product_table ORDER BY "
        when (order) {
            "name" -> query += "name ASC"
            "price" -> query += "price ASC"
            "" -> query += "id DESC"
        }
        getCursor(query).use { cursor ->
            if (cursor.moveToFirst()) {
                do {
                    products.add(
                        Product(
                            cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                            cursor.getString(cursor.getColumnIndexOrThrow("name")),
                            cursor.getDouble(cursor.getColumnIndexOrThrow("price"))
                        )
                    )
                } while (cursor.moveToNext())
            }
        }
        return MutableLiveData(products)
    }
}