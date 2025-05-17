package com.example.compensarshop.core.services

import com.example.compensarshop.core.dto.Product

class CarService {
    companion object{
        private var instance: CarService? = null
        private var productService : ProductService = ProductService.getInstance()

        fun getInstance(): CarService {
            return instance ?: CarService().also { instance = it }
        }
    }

    private val carItems: MutableSet<Long> = mutableSetOf()

    fun getProductsCar(): List<Product> {
        return carItems.mapNotNull { id ->
            productService.getProductById(id)
        }
    }

    fun addProductToCar(id: Long): Boolean{
        if (carItems.contains(id)) {
            return false
        }
        val product = productService.getProductById(id)
        if (product != null) {
            carItems.add(id)
            return true
        }
        return false
    }

    fun removeProductFromCar(id: Long): Boolean {
        return carItems.remove(id)
    }

    fun isProductInCar(id: Long): Boolean {
        return carItems.contains(id)
    }

    fun clearCar() {
        carItems.clear()
    }

    fun getTotalPrice(): Double {
        return carItems.sumOf { id ->
            productService.getProductById(id)?.price ?: 0.0
        }
    }

    fun getCarItemCount(): Int {
        return carItems.size
    }
}