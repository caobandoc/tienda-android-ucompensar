package com.example.compensarshop.ui.app.cart

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.compensarshop.R
import com.example.compensarshop.core.services.CarService

class CarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car)

        val carService: CarService = CarService.getInstance();

        supportFragmentManager.beginTransaction()
            .replace(R.id.fc_car_products, CarProductFragment(carService))
            .commit()

    }
}