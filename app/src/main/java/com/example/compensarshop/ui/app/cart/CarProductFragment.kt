package com.example.compensarshop.ui.app.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.compensarshop.R
import com.example.compensarshop.core.adapter.ProductCarAdapter
import com.example.compensarshop.core.dto.Product

class CarProductFragment(private val productList: List<Product>) : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_car_product_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView : RecyclerView = view.findViewById(R.id.rv_products)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = ProductCarAdapter(productList)
    }
}