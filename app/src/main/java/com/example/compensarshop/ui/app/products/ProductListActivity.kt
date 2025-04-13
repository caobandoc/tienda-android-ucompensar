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
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)

        val productService = ProductService.getInstance();

        supportFragmentManager.beginTransaction()
            .replace(R.id.fc_products, ProductListFragment(productService))
            .commit()

        val imgCar : ImageView = findViewById(R.id.iv_cart)
        imgCar.setOnClickListener {
            val intent = Intent(this, CarActivity::class.java)
            startActivity(intent)
        }
    }
}