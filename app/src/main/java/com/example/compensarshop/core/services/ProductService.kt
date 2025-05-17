package com.example.compensarshop.core.services

import android.content.Context
import com.example.compensarshop.core.dto.Product
import com.example.compensarshop.core.persistence.ProductPersistence

class ProductService private constructor(context: Context){

    private val productPersistence = ProductPersistence(context)

    companion object{
        private var instance: ProductService? = null

        fun getInstance(context: Context): ProductService {
            return instance ?: ProductService(context.applicationContext).also { instance = it }
        }
    }

    fun getProducts(): List<Product> {
        return productPersistence.getProducts()
    }

    fun getProductById(id: Long): Product? {
        return productPersistence.getProductById(id)
    }
}