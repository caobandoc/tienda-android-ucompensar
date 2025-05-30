package com.example.compensarshop.ui.app.item

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.compensarshop.R
import com.example.compensarshop.core.services.CarService
import com.example.compensarshop.core.services.ProductService
import com.example.compensarshop.core.services.StoreService
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class ItemActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var btnAddToCart : Button
    private lateinit var carService : CarService
    private lateinit var productService : ProductService
    private lateinit var storeService : StoreService
    private var productId: Long = -1
    private val numberFormat = NumberFormat.getCurrencyInstance(Locale("es", "CO"))

    // Variable para el mapa
    private var googleMap: GoogleMap? = null
    private var storeLocation: LatLng? = null
    private var storeName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)

        // Inicializar servicios
        carService = CarService.getInstance(this)
        productService = ProductService.getInstance(this)
        storeService = StoreService.getInstance(this)

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

        // Configurar mapa
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Cargar datos del producto
        lifecycleScope.launch {
            try {
                val product = productService.getProductById(productId)

                product?.let {
                    // Mostrar información del producto
                    tvProductName.text = it.name
                    tvProductPrice.text = numberFormat.format(it.price)

                    // Cargar imagen con Glide
                    Glide.with(this@ItemActivity)
                        .load(it.urlImage)
                        .into(ivProductImage)

                    // Cargar información de la tienda si tiene storeId
                    it.storeId?.let { storeId ->
                        loadStoreData(storeId, tvStoreName, tvStoreAddress)
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(this@ItemActivity, "Error al cargar el producto", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
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

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        map.uiSettings.apply {
            isZoomControlsEnabled = true  // Mostrar botones +/- para zoom
            isZoomGesturesEnabled = true  // Permitir zoom con gestos (pellizcar)
            isScrollGesturesEnabled = true  // Permitir desplazamiento con dedos
            isRotateGesturesEnabled = true  // Permitir rotación con gestos
        }

        updateMapWithStoreLocation()
    }

    private fun updateMapWithStoreLocation() {
        // Solo actualizar si tenemos los datos de la tienda y el mapa está listo
        val location = storeLocation
        val map = googleMap

        if (location != null && map != null) {
            map.clear()
            map.addMarker(MarkerOptions()
                .position(location)
                .title(storeName))

            // Mover cámara a la ubicación
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
        }
    }

    private fun loadStoreData(storeId: Long, tvStoreName: TextView, tvStoreAddress: TextView) {
        lifecycleScope.launch {
            try {
                val store = storeService.getStoreById(storeId)
                store?.let { s ->
                    tvStoreName.text = s.name
                    tvStoreAddress.text = s.address

                    // Guardar ubicación de la tienda
                    storeLocation = LatLng(s.latitude, s.longitude)
                    storeName = s.name

                    // Configurar mapa
                    updateMapWithStoreLocation()
                }
            } catch (e: Exception) {
                Toast.makeText(this@ItemActivity, "Error al cargar información de la tienda", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
    }

}