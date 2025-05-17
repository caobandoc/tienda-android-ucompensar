package com.example.compensarshop.core.services

import android.content.Context
import com.example.compensarshop.core.dto.Store
import com.example.compensarshop.core.persistence.StorePersistence

class StoreService private constructor(context: Context) {

    private val storePersistence = StorePersistence(context)

    companion object {
        private var instance: StoreService? = null

        fun getInstance(context: Context): StoreService {
            return instance ?: StoreService(context.applicationContext).also { instance = it }
        }
    }

    fun getStoreById(id: Long): Store? {
        return storePersistence.getStoreById(id)
    }

}