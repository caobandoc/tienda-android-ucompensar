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

class ProductCarAdapter(private val productList: List<Product>) :
    RecyclerView.Adapter<ProductCarAdapter.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private lateinit var productImage : ImageView
        private lateinit var productName : TextView
        private lateinit var productPrice : TextView

        fun bind(product: Product) {
            // busco los elementos del layout
            productImage = itemView.findViewById(R.id.iv_product_image)
            productName = itemView.findViewById(R.id.tv_product_name)
            productPrice = itemView.findViewById(R.id.tv_product_price)

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