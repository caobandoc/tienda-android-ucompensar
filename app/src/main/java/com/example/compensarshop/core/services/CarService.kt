package com.example.compensarshop.core.services

import com.example.compensarshop.core.dto.Product

class CarService {
    companion object{
        private var instance: CarService? = null
        private var carList: List<Long> = listOf(1,3,5)
        private var productService : ProductService = ProductService.getInstance()

        fun getInstance(): CarService {
            if (instance == null) {
                instance = CarService()
            }
            return instance!!
        }
    }

    fun getProductsCar(): List<Product>{
        val products = mutableListOf<Product>()
        for (id in carList) {
            val product = productService.getProductById(id)
            if (product != null) {
                products.add(product)
            }
        }
        return products
    }
}