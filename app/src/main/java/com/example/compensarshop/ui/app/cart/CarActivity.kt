package com.example.compensarshop.ui.app.cart

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.compensarshop.R
import com.example.compensarshop.core.adapter.ProductCarAdapter
import com.example.compensarshop.core.services.CarService
import java.text.NumberFormat
import java.util.Locale

class CarActivity() : AppCompatActivity(), ProductCarAdapter.PriceChangeListener {

    private lateinit var adapter: ProductCarAdapter
    private lateinit var tvTotalPrice: TextView
    private val carService = CarService.getInstance(this)
    private val numberFormat = NumberFormat.getCurrencyInstance(Locale("es", "CO"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car)

        tvTotalPrice = findViewById(R.id.tv_total_price)

        val ivBack = findViewById<ImageView>(R.id.iv_back)
        ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        setupRecyclerView()
        updateTotalPrice()

        val btnBuy = findViewById<TextView>(R.id.btn_buy)
        btnBuy.setOnClickListener {
            // Verificar si hay productos en el carrito
            if (carService.getCarItemCount() > 0){
                showPurchaseConfirmation()
            } else {
                Toast.makeText(this, "No hay productos en el carrito", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun setupRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.rv_car_products)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = ProductCarAdapter(this)
        adapter.addPriceChangeListener(this)
        recyclerView.adapter = adapter
    }

    override fun onPriceChanged(newTotal: Double) {
        // Actualizar el precio total en la vista
        updateTotalPrice()
    }

    private fun updateTotalPrice() {
        // Actualizar el precio total en la vista
        tvTotalPrice.text = numberFormat.format(carService.getTotalPrice())
    }

    private fun showPurchaseConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Confirmar compra")
            .setMessage("¿Deseas finalizar tu compra por ${numberFormat.format(carService.getTotalPrice())}?")
            .setPositiveButton("Comprar") { _, _ ->
                Toast.makeText(this, "Compra realizada con éxito", Toast.LENGTH_SHORT).show()
                carService.clearCar()
                finish()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

}