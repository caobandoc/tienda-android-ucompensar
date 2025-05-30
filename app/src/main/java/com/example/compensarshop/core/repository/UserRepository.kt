package com.example.compensarshop.core.repository

import com.example.compensarshop.core.dto.User

interface UserRepository {
    suspend fun findUserByAuthId(authId: String): User?
    suspend fun findUserByEmail(email: String): User?
    suspend fun saveUser(user: User): User
}