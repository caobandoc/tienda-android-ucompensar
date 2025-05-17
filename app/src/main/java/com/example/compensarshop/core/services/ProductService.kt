package com.example.compensarshop.core.services

import com.example.compensarshop.core.dto.Product

class ProductService {

    companion object{
        const val URL_IMAGE = "https://picsum.photos/200"
        private var instance: ProductService? = null
        private var productList: List<Product> = listOf(
            Product(1,"Producto 1", 10.0, URL_IMAGE, 1),
            Product(2,"Producto 2", 20.0, URL_IMAGE, 1),
            Product(3,"Producto 3", 30.0, URL_IMAGE, 1),
            Product(4,"Producto 4", 40.0, URL_IMAGE, 2),
            Product(5,"Producto 5", 50.0, URL_IMAGE, 2),
            Product(6,"Producto 6", 60.0, URL_IMAGE, 2),
            Product(7,"Producto 7", 70.0, URL_IMAGE, 3),
            Product(8,"Producto 8", 80.0, URL_IMAGE, 3)
        )

        fun getInstance(): ProductService {
            return instance ?: ProductService().also { instance = it }
        }
    }

    fun getProducts(): List<Product> {
        return productList;
    }

    fun getProductById(id: Long): Product? {
        return productList.find { it.id == id }
    }
}