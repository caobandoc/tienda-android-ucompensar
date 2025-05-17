package com.example.compensarshop.core.persistence

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.compensarshop.core.dto.Store
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import org.json.JSONArray
import org.json.JSONObject

// Extensión para DataStore
private val Context.storeDataStore: DataStore<Preferences> by preferencesDataStore(name = "stores")

class StorePersistence(private val context: Context) {

    companion object {
        private val STORES_KEY = stringPreferencesKey("stores_data")

        // Datos por defecto
        private val defaultStores = listOf(
            Store(
                id = 1,
                name = "Tienda Centro",
                latitude = 4.6097102,
                longitude = -74.081749,
                address = "Av. Carrera 7 #32-84, Bogotá"
            ),
            Store(
                id = 2,
                name = "Tienda Norte",
                latitude = 4.6989972,
                longitude = -74.0503368,
                address = "Calle 134 #9-51, Bogotá"
            ),
            Store(
                id = 3,
                name = "Tienda Salitre",
                latitude = 4.6580899,
                longitude = -74.1131808,
                address = "Avenida El Dorado #68D-35, Bogotá"
            )
        )
    }

    // Guardar tiendas
    suspend fun saveStores(stores: List<Store>) {
        val jsonArray = JSONArray()
        stores.forEach { store ->
            val jsonObject = JSONObject().apply {
                put("id", store.id)
                put("name", store.name)
                put("latitude", store.latitude)
                put("longitude", store.longitude)
                put("address", store.address)
            }
            jsonArray.put(jsonObject)
        }

        context.storeDataStore.edit { preferences ->
            preferences[STORES_KEY] = jsonArray.toString()
        }
    }

    // Obtener tiendas
    fun getStores(): List<Store> = runBlocking {
        val storesFlow: Flow<List<Store>> = context.storeDataStore.data.map { preferences ->
            val storesJson = preferences[STORES_KEY]
            if (storesJson.isNullOrEmpty()) {
                // Inicializar con datos por defecto si no hay datos guardados
                saveStores(defaultStores)
                defaultStores
            } else {
                val jsonArray = JSONArray(storesJson)
                List(jsonArray.length()) { i ->
                    val jsonObject = jsonArray.getJSONObject(i)
                    Store(
                        id = jsonObject.getLong("id"),
                        name = jsonObject.getString("name"),
                        latitude = jsonObject.getDouble("latitude"),
                        longitude = jsonObject.getDouble("longitude"),
                        address = jsonObject.getString("address")
                    )
                }
            }
        }

        // Obtener primer valor del flujo
        storesFlow.first()
    }

    // Obtener tienda por ID
    fun getStoreById(id: Long): Store? {
        return getStores().find { it.id == id }
    }
}