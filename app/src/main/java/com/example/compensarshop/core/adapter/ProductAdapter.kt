package com.example.compensarshop.core.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.compensarshop.core.dto.Product
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.compensarshop.R

class ProductAdapter(private val productList: List<Product>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(product: Product) {
            // busco los elementos del layout
            val productImage : ImageView = itemView.findViewById(R.id.iv_product_image)
            val productName : TextView = itemView.findViewById(R.id.tv_product_name)
            val productPrice : TextView = itemView.findViewById(R.id.tv_product_price)

            productName.text = product.name
            productPrice.text = "$${product.price}"

            // Asigno la imagen del producto
            Glide.with(itemView.context)
                .load(product.urlImage)
                .into(productImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_car_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(productList[position])
    }

    override fun getItemCount(): Int = productList.size
}