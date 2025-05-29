package com.example.compensarshop.core.services

import android.content.Context
import com.example.compensarshop.core.dto.Store
import com.example.compensarshop.core.repository.StoreRepository
import com.example.compensarshop.core.rest.StoreRestConsumer

class StoreService private constructor(context: Context) {

    private val storeRepository : StoreRepository = StoreRestConsumer()
//    private val storeRepository : StoreRepository = StoreDataSource(context)


    companion object {
        private var instance: StoreService? = null

        fun getInstance(context: Context): StoreService {
            return instance ?: StoreService(context.applicationContext).also { instance = it }
        }
    }

    suspend fun getStoreById(id: Long): Store? {
        return storeRepository.getStoreById(id)
    }

}