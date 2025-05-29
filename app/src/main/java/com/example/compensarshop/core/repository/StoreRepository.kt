package com.example.compensarshop.core.repository

import com.example.compensarshop.core.dto.Store

fun interface StoreRepository {
    suspend fun getStoreById(id: Long): Store?
}