package com.example.compensarshop.core.repository

import com.example.compensarshop.core.dto.Product

interface ProductRepository {
    suspend fun getProducts(): List<Product>
    suspend fun getProductById(id: Long): Product?
}