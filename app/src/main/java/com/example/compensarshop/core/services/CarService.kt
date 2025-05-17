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

    private val carItems: MutableMap<Long, Int> = mutableMapOf()

    // Metodo para obtener los productos del carrito
    fun getProductsCar(): List<Pair<Product, Int>> {
        return carItems.map { (id, quantity) ->
            val product = productService.getProductById(id)
            Pair(product!!, quantity)
        }
    }

    // Metodo para agregar un producto al carrito
    fun addProductToCar(id: Long): Boolean {
        // Buscamos en el carItems con el id del producto
        val currentProduct = carItems[id]
        // Si el producto ya existe retorna falso
        if (currentProduct != null) {
            return false
        }
        // Si no existe lo agregamos
        carItems[id] = 1
        return true

    }

    fun increaseProductQuantity(id: Long): Boolean {
        // Buscamos en el carItems con el id del producto
        val currentProduct = carItems[id]
        // Si el producto no existe retorna falso
        if (currentProduct == null) {
            return false
        }
        // Si existe aumentamos la cantidad
        carItems[id] = currentProduct + 1
        return true
    }

    // Metodo para aumentar la cantidad de un producto en el carrito
    fun decreaseProductQuantity(id: Long): Boolean {
        val currentQuantity = carItems[id] ?: return false

        // Verificamos si la cantidad es mayor a 1
        if (currentQuantity > 1) {
            carItems[id] = currentQuantity - 1
            return true
        }
        return false
    }

    // Metodo para aumentar la cantidad de un producto en el carrito
    fun removeProductFromCar(id: Long): Boolean {
        return carItems.remove(id) != null
    }

    fun clearCar() {
        carItems.clear()
    }

    fun getTotalPrice(): Double {
        return carItems.entries.sumOf { (id, quantity) ->
            val price = productService.getProductById(id)?.price ?: 0.0
            price * quantity
        }
    }

    fun getCarItemCount(): Int {
        return carItems.values.sum()
    }

}