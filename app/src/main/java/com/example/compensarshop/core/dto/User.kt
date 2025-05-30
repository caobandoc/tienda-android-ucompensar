package com.example.compensarshop.core.dto

data class User(
    val id: Long? = null,
    val name: String,
    val email: String,
    val password: String? = null,
    val profilePictureUrl : String? = null,
    val authId: String? = null
)
