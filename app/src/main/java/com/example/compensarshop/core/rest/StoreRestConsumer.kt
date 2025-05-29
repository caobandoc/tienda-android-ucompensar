package com.example.compensarshop.core.rest

import com.example.compensarshop.core.dto.Store
import com.example.compensarshop.core.repository.StoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class StoreRestConsumer : StoreRepository {

    private val baseUrl: String = "http://10.0.2.2:8080/api/store"

    override suspend fun getStoreById(id: Long): Store? = withContext(Dispatchers.IO){
        try{
            val url = URL("$baseUrl/$id")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.setRequestProperty("Accept", "application/json")

            val responseCode = connection.responseCode
            if(responseCode != HttpURLConnection.HTTP_OK) {
                null
            }

            val reader = BufferedReader(InputStreamReader(connection.inputStream))
            val response = StringBuilder()
            var line: String?

            while (reader.readLine().also { line = it } != null) {
                response.append(line)
            }

            val jsonObject = JSONObject(response.toString())

            Store(
                id = jsonObject.getLong("id"),
                name = jsonObject.getString("name"),
                latitude = jsonObject.getDouble("latitude"),
                longitude = jsonObject.getDouble("longitude"),
                address = jsonObject.getString("address")
            )

        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}