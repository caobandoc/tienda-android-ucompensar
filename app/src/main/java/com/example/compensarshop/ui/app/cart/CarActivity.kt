package com.example.compensarshop.ui.app.cart

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.compensarshop.R
import com.example.compensarshop.core.services.CarService

class CarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car)

        // Inicializar el servicio de carrito
        val carService: CarService = CarService.getInstance();
        val products = carService.getProductsCar()

        // Obtener el fragmento del carrito y pasarle la lista de productos
        supportFragmentManager.beginTransaction()
            .replace(R.id.fc_car_products, CarProductFragment(products))
            .commit()

        // Calcular el precio total
        val tvTotalPrice: TextView = findViewById(R.id.tv_total_price)
        val totalPrice = totalPrice(products.map { it.price })
        tvTotalPrice.text = "$${totalPrice}"

        // Configurar el botón de volver
        val ivBack: ImageView = findViewById(R.id.iv_back)
        ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

    }

    private fun totalPrice(products: List<Double>): Double {
        var total = 0.0
        for (product in products) {
            total += product
        }
        return total
    }

}