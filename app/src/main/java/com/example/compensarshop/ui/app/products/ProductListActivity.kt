package com.example.compensarshop.ui.app.products

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.compensarshop.R
import com.example.compensarshop.core.services.ProductService
import com.example.compensarshop.ui.app.cart.CarActivity

class ProductListActivity : AppCompatActivity(){

    // Variables de la vista
    private lateinit var imgCar: ImageView


    // Inicializo el servicio de productos
    private var productService: ProductService = ProductService.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        // Indico el xml que voy a usar
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)

        // Obtengo la lista de productos
        val products = productService.getProducts()

        // Inicializo el fragmento de la lista de productos
        supportFragmentManager.beginTransaction()
            .replace(R.id.fc_products, ProductListFragment(products))
            .commit()

        // Inicializo el botón de carrito
        imgCar = findViewById(R.id.iv_cart)
        // Le asigno la acción de abrir el carrito
        imgCar.setOnClickListener {
            val intent = Intent(this, CarActivity::class.java)
            startActivity(intent)
        }
    }
}