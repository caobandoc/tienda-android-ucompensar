package com.example.compensarshop.core.services

import com.example.compensarshop.core.dto.Store

class StoreService private constructor() {

    companion object {
        private var instance: StoreService? = null
        private val storeList: List<Store> = listOf(
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

        fun getInstance(): StoreService {
            return instance ?: StoreService().also { instance = it }
        }
    }

    fun getStores(): List<Store> {
        return storeList
    }

    fun getStoreById(id: Long): Store? {
        return storeList.find { it.id == id }
    }

}