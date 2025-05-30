package com.example.compensarshop.core.rest

import com.example.compensarshop.core.dto.User
import com.example.compensarshop.core.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class UserRestConsumer : UserRepository {

    private val baseUrl: String = "http://10.0.2.2:8080/api/user"

    override suspend fun findUserByAuthId(authId: String): User? = withContext(Dispatchers.IO) {
        try {
            val url = URL("$baseUrl/auth/$authId")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.setRequestProperty("Accept", "application/json")

            val responseCode = connection.responseCode
            if (responseCode != HttpURLConnection.HTTP_OK) {
                return@withContext null
            }

            val reader = BufferedReader(InputStreamReader(connection.inputStream))
            val response = StringBuilder()
            var line: String?

            while (reader.readLine().also { line = it } != null) {
                response.append(line)
            }

            parseUser(response.toString())
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun findUserByEmail(email: String): User? = withContext(Dispatchers.IO) {
        try {
            val url = URL("$baseUrl/email/$email")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.setRequestProperty("Accept", "application/json")

            val responseCode = connection.responseCode
            if (responseCode != HttpURLConnection.HTTP_OK) {
                return@withContext null
            }

            val reader = BufferedReader(InputStreamReader(connection.inputStream))
            val response = StringBuilder()
            var line: String?

            while (reader.readLine().also { line = it } != null) {
                response.append(line)
            }

            parseUser(response.toString())
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun saveUser(user: User): User = withContext(Dispatchers.IO) {
        try {
            val url = URL(baseUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("Accept", "application/json")
            connection.doOutput = true

            // Crear JSON para el usuario
            val userJson = JSONObject().apply {
                put("name", user.name)
                put("email", user.email)
                put("password", user.password ?: JSONObject.NULL)
                put("profilePictureUrl", user.profilePictureUrl ?: JSONObject.NULL)
                put("authId", user.authId ?: JSONObject.NULL)
            }

            // Escribir el cuerpo de la solicitud
            val os = connection.outputStream
            os.write(userJson.toString().toByteArray())
            os.flush()
            os.close()

            val responseCode = connection.responseCode
            if (responseCode != HttpURLConnection.HTTP_OK && responseCode != HttpURLConnection.HTTP_CREATED) {
                return@withContext user // Devolver el usuario original si hay error
            }

            val reader = BufferedReader(InputStreamReader(connection.inputStream))
            val response = StringBuilder()
            var line: String?

            while (reader.readLine().also { line = it } != null) {
                response.append(line)
            }

            parseUser(response.toString()) ?: user
        } catch (e: Exception) {
            e.printStackTrace()
            user // Devolver el usuario original en caso de excepción
        }
    }

    private fun parseUser(json: String): User? {
        return try {
            val jsonObject = JSONObject(json)
            User(
                id = jsonObject.getLong("id"),
                name = jsonObject.getString("name"),
                email = jsonObject.getString("email"),
                password = if (jsonObject.has("password") && !jsonObject.isNull("password"))
                    jsonObject.getString("password") else null,
                profilePictureUrl = if (jsonObject.has("profilePictureUrl") && !jsonObject.isNull("profilePictureUrl"))
                    jsonObject.getString("profilePictureUrl") else null,
                authId = if (jsonObject.has("authId") && !jsonObject.isNull("authId"))
                    jsonObject.getString("authId") else null
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}