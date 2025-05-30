package com.example.compensarshop.core.services

import android.content.Context
import com.example.compensarshop.core.dto.Product

class CarService private constructor(context: Context){
    companion object{
        private var instance: CarService? = null

        fun getInstance(context: Context): CarService {
            return instance ?: CarService(context.applicationContext).also { instance = it }
        }
    }

    private val carItems: MutableMap<Long, Int> = mutableMapOf()
    private val productService = ProductService.getInstance(context)

    // Metodo para obtener los productos del carrito
    suspend fun getProductsCar(): List<Pair<Product, Int>> {
        val result = mutableListOf<Pair<Product, Int>>()
        for ((id, quantity) in carItems) {
            val product = productService.getProductById(id)
            product?.let {
                result.add(Pair(it, quantity))
            }
        }
        return result
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

    suspend fun getTotalPrice(): Double {
        var total = 0.0
        for ((id, quantity) in carItems) {
            val price = productService.getProductById(id)?.price ?: 0.0
            total += price * quantity
        }
        return total
    }

    fun getCarItemCount(): Int {
        return carItems.values.sum()
    }

}