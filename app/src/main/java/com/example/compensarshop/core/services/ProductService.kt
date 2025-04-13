package com.example.compensarshop.core.services

import com.example.compensarshop.core.dto.Product

class ProductService {

    companion object{
        const val URL_IMAGE = "https://placehold.co/200"
        private var instance: ProductService? = null
        private var productList: List<Product> = listOf(
            Product(1,"Producto 1", 10.0, URL_IMAGE),
            Product(2,"Producto 2", 20.0, URL_IMAGE),
            Product(3,"Producto 3", 30.0, URL_IMAGE),
            Product(4,"Producto 4", 40.0, URL_IMAGE),
            Product(5,"Producto 5", 50.0, URL_IMAGE),
            Product(6,"Producto 6", 60.0, URL_IMAGE),
            Product(7,"Producto 7", 70.0, URL_IMAGE),
            Product(8,"Producto 8", 80.0, URL_IMAGE)
        )

        fun getInstance(): ProductService {
            if (instance == null) {
                instance = ProductService()
            }
            return instance!!
        }
    }


    fun getProducts(): List<Product> {
        return productList;
    }

    fun getProductById(id: Long): Product? {
        return productList.find { it.id == id }
    }
}