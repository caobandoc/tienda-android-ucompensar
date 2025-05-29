package com.example.compensarshop.core.services

import android.content.Context
import com.example.compensarshop.core.dto.User
import com.example.compensarshop.core.datasource.UserDataSource
import kotlinx.coroutines.runBlocking

class LoginService private constructor(context: Context) {

    private val userDataSource = UserDataSource(context)

    companion object {

        private var instance: LoginService? = null

        fun getInstance(context: Context): LoginService {
            return instance ?: LoginService(context.applicationContext).also { instance = it }
        }
    }

    // Login con credenciales locales
    fun loginWithCredentials(email: String, password: String): Boolean {
        if (email.isEmpty() || password.isEmpty()) return false

        val user = userDataSource.findUserByEmail(email) ?: return false

        // Verificar si es usuario local y la contraseña coincide
        if (user.password == password) {
            return true
        }

        return false
    }

    // Login con Google
    fun loginWithGoogle(email: String, name: String, googleId: String, profilePictureUrl: String?): Boolean {
        // Buscar usuario por Google ID
        var user = userDataSource.findUserByAuthId(googleId)

        if(user == null){
            user = userDataSource.findUserByEmail(email)

            if(user == null){
                // Usuario nuevo
                val newUser = User(
                    id = System.currentTimeMillis(),
                    name = name,
                    email = email,
                    password = null,
                    profilePictureUrl = profilePictureUrl,
                    authId = googleId
                )

                runBlocking {
                    userDataSource.saveUser(newUser)
                }
            }else{
                // Existe un usuario, actualizar Google ID
                val updatedUser = user.copy(
                    authId = googleId,
                    profilePictureUrl = profilePictureUrl
                )

                runBlocking {
                    userDataSource.saveUser(updatedUser)
                }
            }
        }

        return true

    }

    fun registerUserLocal(name: String, lastName: String, email: String, password: String) {
        val user = User(
            id = System.currentTimeMillis(),
            name = "$name $lastName",
            email = email,
            password = password
        )

        runBlocking {
            userDataSource.saveUser(user)
        }
    }

    fun existsUser(email: String): Boolean {
        return userDataSource.findUserByEmail(email) != null
    }


}