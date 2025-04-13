package com.example.compensarshop.ui.app.products

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.compensarshop.R
import com.example.compensarshop.core.services.ProductService
import com.example.compensarshop.ui.app.cart.CarActivity

class ProductListActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        // Indico el xml que voy a usar
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)

        // Inicializo el servicio de productos
        val productService = ProductService.getInstance();
        val products = productService.getProducts()

        // Inicializo el fragmento de la lista de productos
        supportFragmentManager.beginTransaction()
            .replace(R.id.fc_products, ProductListFragment(products))
            .commit()

        // Inicializo el botón de carrito
        val imgCar : ImageView = findViewById(R.id.iv_cart)
        // Le asigno la acción de abrir el carrito
        imgCar.setOnClickListener {
            val intent = Intent(this, CarActivity::class.java)
            startActivity(intent)
        }
    }
}