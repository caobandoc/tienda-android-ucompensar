package com.example.compensarshop.core.dto

data class Product(
    val id: Long,
    val name: String,
    val price: Double,
    val urlImage: String,
    val storeId: Long? = null,
)