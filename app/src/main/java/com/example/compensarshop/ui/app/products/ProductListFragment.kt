package com.example.compensarshop.ui.app.products

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.compensarshop.R
import com.example.compensarshop.core.adapter.ProductAdapter

class ProductListFragment(private val context: Context) : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_product_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // obtengo el recyclerView y la lista de productos
        val recyclerView : RecyclerView = view.findViewById(R.id.rv_products)

        // Configuro el recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // asigno el adapter
        recyclerView.adapter = ProductAdapter(context)
    }
}