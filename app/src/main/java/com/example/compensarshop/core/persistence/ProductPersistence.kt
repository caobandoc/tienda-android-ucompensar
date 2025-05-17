package com.example.compensarshop.core.persistence

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.compensarshop.core.dto.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import org.json.JSONArray
import org.json.JSONObject

// Extension para DataStore
private val Context.productDataStore: DataStore<Preferences> by preferencesDataStore(name = "products")

class ProductPersistence(private val context: Context) {
    companion object {
        private val PRODUCTS_KEY = stringPreferencesKey("products_data")
        private const val URL_IMAGE = "https://picsum.photos/200"

        //Datos por defecto
        private val defaultProducts = listOf(
            Product(1,"Producto 1", 10.0, URL_IMAGE, 1),
            Product(2,"Producto 2", 20.0, URL_IMAGE, 1),
            Product(3,"Producto 3", 30.0, URL_IMAGE, 1),
            Product(4,"Producto 4", 40.0, URL_IMAGE, 2),
            Product(5,"Producto 5", 50.0, URL_IMAGE, 2),
            Product(6,"Producto 6", 60.0, URL_IMAGE, 2),
            Product(7,"Producto 7", 70.0, URL_IMAGE, 3),
            Product(8,"Producto 8", 80.0, URL_IMAGE, 3)
        )
    }

    // Guardar productos en DataStore
    suspend fun saveProducts(products: List<Product>) {
        val jsonArray = JSONArray()
        products.forEach { product ->
            val jsonObject = JSONObject().apply {
                put("id", product.id)
                put("name", product.name)
                put("price", product.price)
                put("urlImage", product.urlImage)
                put("storeId", product.storeId)
            }
            jsonArray.put(jsonObject)
        }

        context.productDataStore.edit { preferences ->
            preferences[PRODUCTS_KEY] = jsonArray.toString()
        }
    }

    // Obtener productos
    fun getProducts(): List<Product> = runBlocking {
        val productsFlow: Flow<List<Product>> = context.productDataStore.data.map { preferences ->
            val productsJson = preferences[PRODUCTS_KEY]
            if (productsJson.isNullOrEmpty()) {
                // Inicializar con datos por defecto si no hay datos guardados
                saveProducts(defaultProducts)
                defaultProducts
            } else {
                val jsonArray = JSONArray(productsJson)
                List(jsonArray.length()) { i ->
                    val jsonObject = jsonArray.getJSONObject(i)
                    Product(
                        id = jsonObject.getLong("id"),
                        name = jsonObject.getString("name"),
                        price = jsonObject.getDouble("price"),
                        urlImage = jsonObject.getString("urlImage"),
                        storeId = jsonObject.getLong("storeId")
                    )
                }
            }
        }

        // Obtener primer valor del flujo
        productsFlow.first()
    }

    // Obtener producto por ID
    fun getProductById(id: Long): Product? {
        return getProducts().find { it.id == id }
    }
}