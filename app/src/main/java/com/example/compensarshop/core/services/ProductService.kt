package com.example.compensarshop.core.services

import android.content.Context
import com.example.compensarshop.core.dto.Product
import com.example.compensarshop.core.repository.ProductRepository
import com.example.compensarshop.core.rest.ProductRestConsumer

class ProductService private constructor(context: Context){

    private val productRepository: ProductRepository = ProductRestConsumer()
//    private val productRepository : ProductRepository = ProductDataSource(context)

    companion object{
        private var instance: ProductService? = null

        fun getInstance(context: Context): ProductService {
            return instance ?: ProductService(context.applicationContext).also { instance = it }
        }
    }

    suspend fun getProducts(): List<Product> {
        return productRepository.getProducts()
    }

    suspend fun getProductById(id: Long): Product? {
        return productRepository.getProductById(id)
    }
}