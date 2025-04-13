package com.example.compensarshop.core.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.compensarshop.core.dto.Product
import androidx.recyclerview.widget.RecyclerView
import com.example.compensarshop.R

class ProductCarAdapter(private val productList: List<Product>) :
    RecyclerView.Adapter<ProductCarAdapter.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(product: Product) {
            val productName : TextView = itemView.findViewById(R.id.tv_product_name)
            val productPrice : TextView = itemView.findViewById(R.id.tv_product_price)
            productName.text = product.name
            productPrice.text = "$${product.price}"
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