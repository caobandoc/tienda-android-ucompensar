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
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.compensarshop.R
import com.example.compensarshop.core.dto.Product
import com.example.compensarshop.core.services.CarService
import java.text.NumberFormat
import java.util.Locale

class ProductCarAdapter(private val context: Context) :
    RecyclerView.Adapter<ProductCarAdapter.ProductCarViewHolder>() {

    private val carService = CarService.getInstance(context)
    private val numberFormat = NumberFormat.getCurrencyInstance(Locale("es", "CO"))

    // Obtener datos del carrito
    private var productList: List<Pair<Product, Int>> = carService.getProductsCar()

    // Metodo para refrescar datos
    @SuppressLint("NotifyDataSetChanged") // no es lo ideal, pero es una forma rápida de refrescar la vista
    fun refreshData() {
        productList = carService.getProductsCar()
        notifyDataSetChanged()
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
            carService.increaseProductQuantity(product.id)
            refreshData()
            notifyPriceChangeListeners()
        }

        // Disminuir cantidad
        holder.btnDecrease.setOnClickListener {
            carService.decreaseProductQuantity(product.id)
            refreshData()
            notifyPriceChangeListeners()
        }

        // Eliminar producto
        holder.btnRemove.setOnClickListener {
            carService.removeProductFromCar(product.id)
            refreshData()
            notifyPriceChangeListeners()
        }
    }

    // Interfaz para notificar cambios en el precio total
    fun interface PriceChangeListener {
        fun onPriceChanged(newTotal: Double)
    }

    private val priceChangeListeners = mutableListOf<PriceChangeListener>()

    // Metodo para agregar un listener
    fun addPriceChangeListener(listener: PriceChangeListener) {
        priceChangeListeners.add(listener)
    }

    private fun notifyPriceChangeListeners() {
        // Notificar a todos los listeners sobre el cambio en el precio total
        val total = carService.getTotalPrice()
        priceChangeListeners.forEach { it.onPriceChanged(total) }
    }

}