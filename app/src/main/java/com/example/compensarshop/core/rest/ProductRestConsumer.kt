package com.example.compensarshop.core.rest

import com.example.compensarshop.core.dto.Product
import com.example.compensarshop.core.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class ProductRestConsumer : ProductRepository {

    private val baseUrl: String = "http://10.0.2.2:8080/api/product"

    override suspend fun getProducts(): List<Product> = withContext(Dispatchers.IO) {
        try {
            val url = URL(baseUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.setRequestProperty("Accept", "application/json")

            val responseCode = connection.responseCode
            if (responseCode != HttpURLConnection.HTTP_OK) {
                emptyList()
            } else {
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                val response = StringBuilder()
                var line: String?

                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }

                val jsonArray = JSONArray(response.toString())
                val products = mutableListOf<Product>()

                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    val product = Product(
                        id = jsonObject.getLong("id"),
                        name = jsonObject.getString("name"),
                        price = jsonObject.getDouble("price"),
                        urlImage = jsonObject.optString("urlImage", "https://picsum.photos/200"),
                        storeId = jsonObject.optLong("storeId", 0)
                    )
                    products.add(product)
                }

                products
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getProductById(id: Long): Product? = withContext(Dispatchers.IO) {
        try {
            val url = URL("$baseUrl/$id")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.setRequestProperty("Accept", "application/json")

            val responseCode = connection.responseCode
            if (responseCode != HttpURLConnection.HTTP_OK) {
                null
            } else {
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                val response = StringBuilder()
                var line: String?

                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }

                val jsonObject = org.json.JSONObject(response.toString())

                Product(
                    id = jsonObject.getLong("id"),
                    name = jsonObject.getString("name"),
                    price = jsonObject.getDouble("price"),
                    urlImage = jsonObject.optString("urlImage", "https://picsum.photos/200"),
                    storeId = jsonObject.optLong("storeId", 0)
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}