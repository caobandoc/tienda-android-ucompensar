package com.example.compensarshop.ui.app.cart

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.compensarshop.R
import com.example.compensarshop.core.adapter.ProductCarAdapter
import com.example.compensarshop.core.services.CarService
import kotlinx.coroutines.launch
import java.io.File
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CarActivity : AppCompatActivity(), ProductCarAdapter.PriceChangeListener {

    private lateinit var adapter: ProductCarAdapter
    private lateinit var tvTotalPrice: TextView
    private val carService = CarService.getInstance(this)
    private val numberFormat = NumberFormat.getCurrencyInstance(Locale("es", "CO"))

    // Variables para la captura de imagen
    private var photoURI: Uri? = null
    private var photoPath: String = ""

    // Registro de callback para permisos de cámara
    private val requestCameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                takePhoto()
            } else {
                Toast.makeText(this, "Se requiere permiso de cámara para continuar", Toast.LENGTH_LONG).show()
            }
        }

    // Registro de callback para la captura de foto
    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                proceedWithPurchase()
            } else {
                Toast.makeText(this, "No se pudo capturar la foto", Toast.LENGTH_SHORT).show()
            }
        }

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
            if (carService.getCarItemCount() > 0) {
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
        tvTotalPrice.text = numberFormat.format(newTotal)
    }

    private fun updateTotalPrice() {
        // Actualizar el precio total usando una corrutina
        lifecycleScope.launch {
            val total = carService.getTotalPrice()
            tvTotalPrice.text = numberFormat.format(total)
        }
    }

    private fun showPurchaseConfirmation() {
        lifecycleScope.launch {
            val totalPrice = carService.getTotalPrice()
            AlertDialog.Builder(this@CarActivity)
                .setTitle("Confirmar compra")
                .setMessage("¿Deseas finalizar tu compra por ${numberFormat.format(totalPrice)}?")
                .setPositiveButton("Comprar") { _, _ ->
                    checkCameraPermission()
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }
    }

    private fun checkCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED -> {
                takePhoto()
            }
            else -> {
                requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun takePhoto() {
        // Crear archivo para guardar la imagen
        val imageFile = createImageFile()

        // Para Android 10 y superiores, usamos MediaStore
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, "CompensarShop_${System.currentTimeMillis()}.jpg")
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }
            photoURI = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        } else {
            // Para versiones anteriores usamos FileProvider
            photoURI = FileProvider.getUriForFile(
                this,
                "${applicationContext.packageName}.fileprovider",
                imageFile
            )
        }

        photoURI?.let {
            takePictureLauncher.launch(it)
        }
    }

    private fun createImageFile(): File {
        // Crear nombre de archivo único
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "JPEG_${timeStamp}_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File.createTempFile(
            imageFileName,
            ".jpg",
            storageDir
        ).apply {
            photoPath = absolutePath
        }
    }

    private fun proceedWithPurchase() {
        // Mostrar imagen capturada o mensaje de confirmación
        AlertDialog.Builder(this)
            .setTitle("Compra confirmada")
            .setMessage("Imagen guardada correctamente. Tu compra ha sido procesada.")
            .setPositiveButton("Aceptar") { _, _ ->
                Toast.makeText(this, "Compra realizada con éxito", Toast.LENGTH_SHORT).show()
                carService.clearCar()
                finish()
            }
            .show()
    }
}