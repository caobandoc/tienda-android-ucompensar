package com.example.compensarshop.core.services

import android.content.Context
import com.example.compensarshop.core.dto.User
import com.example.compensarshop.core.repository.UserRepository
import com.example.compensarshop.core.rest.UserRestConsumer
import kotlinx.coroutines.runBlocking

class LoginService private constructor(context: Context) {

//    private val userRepository : UserRepository = UserDataSource(context)
    private val userRepository: UserRepository = UserRestConsumer()

    companion object {

        private var instance: LoginService? = null

        fun getInstance(context: Context): LoginService {
            return instance ?: LoginService(context.applicationContext).also { instance = it }
        }
    }

    // Login con credenciales locales
    suspend fun loginWithCredentials(email: String, password: String): Boolean {
        if (email.isEmpty() || password.isEmpty()) return false

        val user = userRepository.findUserByEmail(email) ?: return false

        // Verificar si es usuario local y la contraseña coincide
        if (user.password == password) {
            return true
        }

        return false
    }

    // Login con Google
    suspend fun loginWithGoogle(email: String, name: String, googleId: String, profilePictureUrl: String?): Boolean {
        // Buscar usuario por Google ID
        var user = userRepository.findUserByAuthId(googleId)

        if(user == null){
            user = userRepository.findUserByEmail(email)

            if(user == null){
                // Usuario nuevo
                val newUser = User(
                    name = name,
                    email = email,
                    password = null,
                    profilePictureUrl = profilePictureUrl,
                    authId = googleId
                )

                runBlocking {
                    userRepository.saveUser(newUser)
                }
            }else{
                // Existe un usuario, actualizar Google ID
                val updatedUser = user.copy(
                    authId = googleId,
                    profilePictureUrl = profilePictureUrl
                )

                runBlocking {
                    userRepository.saveUser(updatedUser)
                }
            }
        }

        return true

    }

    suspend fun registerUserLocal(name: String, lastName: String, email: String, password: String) {
        val user = User(
            id = System.currentTimeMillis(),
            name = "$name $lastName",
            email = email,
            password = password
        )

        runBlocking {
            userRepository.saveUser(user)
        }
    }

    suspend fun existsUser(email: String): Boolean {
        return userRepository.findUserByEmail(email) != null
    }


}