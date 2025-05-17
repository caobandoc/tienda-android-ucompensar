package com.example.compensarshop.ui.app.item

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.compensarshop.R
import com.example.compensarshop.core.services.CarService
import com.example.compensarshop.core.services.ProductService
import com.example.compensarshop.core.services.StoreService

class ItemActivity: AppCompatActivity() {
    private lateinit var btnAddToCart: Button
    private val carService = CarService.getInstance()
    private val productService = ProductService.getInstance()
    private val storeService = StoreService.getInstance()
    private var productId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)

        // Inicializar vistas
        val ivBack = findViewById<ImageView>(R.id.iv_back)
        val ivProductImage = findViewById<ImageView>(R.id.iv_product_detail_image)
        val tvProductName = findViewById<TextView>(R.id.tv_product_detail_name)
        val tvProductPrice = findViewById<TextView>(R.id.tv_product_detail_price)
        val tvStoreName = findViewById<TextView>(R.id.tv_store_name)
        val tvStoreAddress = findViewById<TextView>(R.id.tv_store_address)
        btnAddToCart = findViewById(R.id.btn_add_to_cart)

        // Obtener el ID del producto desde el intent
        productId = intent.getLongExtra("PRODUCT_ID", -1)
        if (productId == -1L) {
            finish()
            return
        }

        // Cargar datos del producto
        val product = productService.getProductById(productId)
        product?.let {
            // Mostrar información del producto
            tvProductName.text = it.name
            tvProductPrice.text = "$${it.price}"

            // Cargar imagen con Glide
            Glide.with(this)
                .load(it.urlImage)
                .into(ivProductImage)

            // Cargar información de la tienda si tiene storeId
            it.storeId?.let { storeId ->
                val store = storeService.getStoreById(storeId)
                store?.let { s ->
                    tvStoreName.text = s.name
                    tvStoreAddress.text = s.address
                }
            }

        }

        // Configurar evento de botón volver
        ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Configurar evento de botón añadir al carrito
        btnAddToCart.setOnClickListener {
            if (carService.addProductToCar(productId)) {
                Toast.makeText(this, "Producto añadido al carrito", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(this, "El producto ya está en el carrito", Toast.LENGTH_SHORT)
                    .show()
            }

        }
    }

}