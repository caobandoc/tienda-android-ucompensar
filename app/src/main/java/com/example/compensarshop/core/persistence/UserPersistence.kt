package com.example.compensarshop.core.persistence

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.compensarshop.core.dto.User
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import org.json.JSONArray
import org.json.JSONObject

// Extensión para DataStore
private val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(name = "users")

class UserPersistence(private val context: Context) {

    companion object {
        private val USERS_KEY = stringPreferencesKey("users_data")
    }

    // Guardar usuario (nuevo o actualización)
    suspend fun saveUser(user: User) {
        val users = getUsers().toMutableList()

        // Buscar si existe por email o por authId
        val existingIndex = users.indexOfFirst {
            it.email == user.email || (user.authId != null && it.authId == user.authId)
        }

        if (existingIndex != -1) {
            users[existingIndex] = user
        } else {
            users.add(user)
        }

        // Guardar la lista actualizada
        val jsonArray = JSONArray()
        users.forEach { u ->
            val jsonObject = JSONObject().apply {
                put("id", u.id)
                put("name", u.name)
                put("email", u.email)
                put("password", u.password)
                put("profilePictureUrl", u.profilePictureUrl ?: "")
                put("authId", u.authId ?: "")
            }
            jsonArray.put(jsonObject)
        }

        context.userDataStore.edit { preferences ->
            preferences[USERS_KEY] = jsonArray.toString()
        }
    }

    // Obtener todos los usuarios
    fun getUsers(): List<User> = runBlocking {
        context.userDataStore.data.map { preferences ->
            val usersJson = preferences[USERS_KEY]
            if (usersJson.isNullOrEmpty()) {
                emptyList()
            } else {
                val jsonArray = JSONArray(usersJson)
                List(jsonArray.length()) { i ->
                    val jsonObject = jsonArray.getJSONObject(i)
                    User(
                        id = jsonObject.getLong("id"),
                        name = jsonObject.getString("name"),
                        email = jsonObject.getString("email"),
                        password = jsonObject.getString("password"),
                        profilePictureUrl = jsonObject.optString("profilePictureUrl").takeIf { it.isNotEmpty() },
                        authId = jsonObject.optString("authId").takeIf { it.isNotEmpty() }
                    )
                }
            }
        }.first()
    }

    // Buscar usuario por email
    fun findUserByEmail(email: String): User? {
        return getUsers().find { it.email == email }
    }

    // Buscar usuario por authId de Google
    fun findUserByAuthId(authId: String): User? {
        return getUsers().find { it.authId == authId }
    }
}