package com.example.compensarshop.core.dto

data class Store(
    val id: Long,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val address: String
)

