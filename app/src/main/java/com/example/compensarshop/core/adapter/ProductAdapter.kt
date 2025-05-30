package com.example.compensarshop.core.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.compensarshop.R
import com.example.compensarshop.core.dto.Product
import com.example.compensarshop.core.services.CarService
import com.example.compensarshop.core.services.ProductService
import com.example.compensarshop.ui.app.item.ItemActivity
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class ProductAdapter(context: Context) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private var productService : ProductService = ProductService.getInstance(context)
    private var carService : CarService = CarService.getInstance(context)
    private val numberFormat = NumberFormat.getCurrencyInstance(Locale("es", "CO"))

    // Lista de productos
    private var productList: List<Product> = emptyList()

    // Inicializar con datos
    init {
        (context as? LifecycleOwner)?.lifecycleScope?.launch {
            updateProducts()
        }
    }

    suspend fun updateProducts() {
        try {
            productList = productService.getProducts()
            notifyDataSetChanged()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Referencias a los elementos del layout
        val productImage: ImageView = itemView.findViewById(R.id.iv_product_image)
        val productName: TextView = itemView.findViewById(R.id.tv_product_name)
        val productPrice: TextView = itemView.findViewById(R.id.tv_product_price)
        val btnAdd: Button = itemView.findViewById(R.id.btn_add)
        val btnView: Button = itemView.findViewById(R.id.btn_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]

        // Configurar los elementos de la vista
        holder.productName.text = product.name
        holder.productPrice.text = numberFormat.format(product.price)

        // Cargar imagen con Glide
        Glide.with(holder.itemView.context)
            .load(product.urlImage)
            .into(holder.productImage)

        // Configurar botón añadir
        holder.btnAdd.setOnClickListener {
            if (carService.addProductToCar(product.id)) {
                Toast.makeText(
                    holder.itemView.context,
                    "${product.name} agregado al carrito",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    holder.itemView.context,
                    "El producto ${product.name} ya esta en el carrito",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // Configurar botón ver
        holder.btnView.setOnClickListener {
            val intent = Intent(holder.itemView.context, ItemActivity::class.java)
            intent.putExtra("PRODUCT_ID", product.id)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = productList.size

}