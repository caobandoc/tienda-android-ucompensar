package com.example.compensarshop.core.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.compensarshop.R
import com.example.compensarshop.core.dto.Product
import com.example.compensarshop.core.services.CarService
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class ProductCarAdapter(private val context: Context) :
    RecyclerView.Adapter<ProductCarAdapter.ProductCarViewHolder>() {

    private val carService = CarService.getInstance(context)
    private val numberFormat = NumberFormat.getCurrencyInstance(Locale("es", "CO"))

    // Lista de productos en el carrito
    private var productList: List<Pair<Product, Int>> = emptyList()

    // Inicializar datos
    init {
        // Verificar si el contexto es un LifecycleOwner para usar lifecycleScope
        (context as? LifecycleOwner)?.lifecycleScope?.launch {
            refreshData()
        }
    }

    // Método suspendido para refrescar datos
    @SuppressLint("NotifyDataSetChanged")
    suspend fun refreshData() {
        productList = carService.getProductsCar()
        notifyDataSetChanged()
        notifyPriceChangeListeners()
    }

    // Método no suspendido que lanza una corrutina
    fun updateData() {
        (context as? LifecycleOwner)?.lifecycleScope?.launch {
            refreshData()
        }
    }

    class ProductCarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Referencias a los elementos de la vista
        val productImage: ImageView = itemView.findViewById(R.id.iv_product_image)
        val productName: TextView = itemView.findViewById(R.id.tv_product_name)
        val productPrice: TextView = itemView.findViewById(R.id.tv_product_price)
        val quantity: TextView = itemView.findViewById(R.id.tv_quantity)
        val subtotal: TextView = itemView.findViewById(R.id.tv_subtotal)
        val btnIncrease: Button = itemView.findViewById(R.id.btn_increase)
        val btnDecrease: Button = itemView.findViewById(R.id.btn_decrease)
        val btnRemove: ImageButton = itemView.findViewById(R.id.btn_remove)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductCarViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_car_product, parent, false)
        return ProductCarViewHolder(view)
    }

    override fun getItemCount(): Int = productList.size

    override fun onBindViewHolder(holder: ProductCarViewHolder, position: Int) {
        // Obtener el producto y la cantidad
        val (product, quantity) = productList[position]

        // Configurar los elementos de la vista
        holder.productName.text = product.name
        holder.productPrice.text = numberFormat.format(product.price)
        holder.quantity.text = quantity.toString()

        // Calcular y mostrar subtotal
        val subtotal = product.price * quantity
        holder.subtotal.text = numberFormat.format(subtotal)

        // Cargar imagen con Glide
        Glide.with(holder.itemView.context)
            .load(product.urlImage)
            .into(holder.productImage)

        // Configurar botones
        // Aumentar cantidad
        holder.btnIncrease.setOnClickListener {
            if (carService.increaseProductQuantity(product.id)) {
                updateData()
            }
        }

        // Disminuir cantidad
        holder.btnDecrease.setOnClickListener {
            if (carService.decreaseProductQuantity(product.id)) {
                updateData()
            }
        }

        // Eliminar producto
        holder.btnRemove.setOnClickListener {
            if (carService.removeProductFromCar(product.id)) {
                updateData()
            }
        }
    }

    // Interfaz para notificar cambios en el precio total
    fun interface PriceChangeListener {
        fun onPriceChanged(newTotal: Double)
    }

    private val priceChangeListeners = mutableListOf<PriceChangeListener>()

    // Método para agregar un listener
    fun addPriceChangeListener(listener: PriceChangeListener) {
        priceChangeListeners.add(listener)
        // Notificar inmediatamente el precio actual
        (context as? LifecycleOwner)?.lifecycleScope?.launch {
            val total = carService.getTotalPrice()
            listener.onPriceChanged(total)
        }
    }

    private suspend fun notifyPriceChangeListeners() {
        // Notificar a todos los listeners sobre el cambio en el precio total
        val total = carService.getTotalPrice()
        priceChangeListeners.forEach { it.onPriceChanged(total) }
    }

}