package com.example.compensarshop.core.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.compensarshop.R
import com.example.compensarshop.core.dto.Product
import com.example.compensarshop.core.services.CarService
import com.example.compensarshop.ui.app.item.ItemActivity

class ProductAdapter(private val productList: List<Product>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private lateinit var productImage : ImageView
        private lateinit var productName : TextView
        private lateinit var productPrice : TextView
        private lateinit var btnAdd : Button
        private lateinit var btnView: Button

        // Instancio el servicio de carrito
        private var carService : CarService = CarService.getInstance()

        fun bind(product: Product) {
            // busco los elementos del layout
            productImage = itemView.findViewById(R.id.iv_product_image)
            productName = itemView.findViewById(R.id.tv_product_name)
            productPrice = itemView.findViewById(R.id.tv_product_price)
            btnAdd = itemView.findViewById(R.id.btn_add)
            btnView = itemView.findViewById(R.id.btn_view)

            productName.text = product.name
            productPrice.text = "$${product.price}"

            // Asigno la imagen del producto
            Glide.with(itemView.context)
                .load(product.urlImage)
                .into(productImage)

            // Asigno el evento click al boton
            btnAdd.setOnClickListener {
                if (carService.addProductToCar(product.id)) {
                    // Toast indicando que se agrego el producto
                    Toast.makeText(
                        itemView.context,
                        "${product.name} agregado al carrito",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        itemView.context,
                        "${product.name} ya esta en el carrito",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            // Asigno el evento click al boton
            btnView.setOnClickListener {
                val intent = Intent(itemView.context, ItemActivity::class.java)
                intent.putExtra("PRODUCT_ID", product.id)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(productList[position])
    }

    override fun getItemCount(): Int = productList.size
}