package com.example.compensarshop.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import com.example.compensarshop.BuildConfig
import com.example.compensarshop.R
import com.example.compensarshop.core.services.LoginService
import com.example.compensarshop.ui.app.products.ProductListActivity
import com.example.compensarshop.ui.auth.register.RegisterActivity
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity(){

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnRegister: Button
    private lateinit var btnGoogleSignIn: Button
    private val tag = "GoogleSignIn"

    private lateinit var credentialManager: CredentialManager
    private lateinit var loginService: LoginService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etEmail = findViewById(R.id.et_user)
        etPassword = findViewById(R.id.et_password)
        btnLogin = findViewById(R.id.btn_login)
        btnRegister = findViewById(R.id.btn_register)
        btnGoogleSignIn = findViewById(R.id.btn_google_sign_in)

        credentialManager = CredentialManager.create(this)
        loginService = LoginService.getInstance(this)

        btnLogin.setOnClickListener {
            loginWithCredentials()
        }

        btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        btnGoogleSignIn.setOnClickListener {
            signIn()
        }

    }

    private fun loginWithCredentials(){
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor ingrese todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        if (loginService.loginWithCredentials(email, password)) {
            // Navegar a la pantalla principal
            navigateToProductList()
            finish()
        } else {
            Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
        }
    }

    private fun signIn() {
        CoroutineScope(Dispatchers.Main).launch {
            try{
                // Configurar la opción de Google ID
                val googleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(BuildConfig.GOOGLE_WEB_CLIENT_ID)
                    .build()

                // Crear solicitud de credencial
                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                // Obtener credenciales
                val response = credentialManager.getCredential(
                    request = request,
                    context = this@LoginActivity
                )

                handleSignIn(response)
            } catch (e: GetCredentialException) {
                Log.e(tag, "Error al obtener credenciales: ${e.message}")
                Toast.makeText(
                    this@LoginActivity,
                    "Error al iniciar sesión con Google",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun handleSignIn(response: GetCredentialResponse) {
        val credential = response.credential

        if (credential is CustomCredential &&
            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            try {
                // Parsear el token de Google
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

                // Aquí puedes obtener información del usuario
                val userId = googleIdTokenCredential.idToken
                val name = googleIdTokenCredential.displayName
                val profilePictureUri = googleIdTokenCredential.profilePictureUri
                val email = googleIdTokenCredential.id

                Log.d(tag, "Inicio de sesión exitoso: $name, $email")

                // Autenticar con nuestro servicio
                if (loginService.loginWithGoogle(email, name.toString(), userId,
                        profilePictureUri.toString())) {
                    navigateToProductList()
                    finish()
                } else {
                    Toast.makeText(
                        this,
                        "Error al procesar la autenticación de Google",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: GoogleIdTokenParsingException) {
                Log.e(tag, "Error al analizar el token: ${e.message}")
                Toast.makeText(
                    this,
                    "Error al procesar la información de Google",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Log.e(tag, "Tipo de credencial no reconocido")
            Toast.makeText(
                this,
                "Tipo de credencial no compatible",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun navigateToProductList() {
        val intent = Intent(this, ProductListActivity::class.java)
        startActivity(intent)
    }

}