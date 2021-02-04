package com.example.fooddelivery.ui.main.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.data.database.entity.Product
import com.example.fooddelivery.R
import com.example.fooddelivery.ui.main.utils.ProjectEventListeners

/**
 * [RecyclerView.Adapter] that can display list of [Product].
 */
class MyProductRecyclerViewAdapter(
    private val values: ArrayList<Product>,
    private val callback: ProjectEventListeners.CartEvents
) : RecyclerView.Adapter<MyCartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCartViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MyCartViewHolder(
            DataBindingUtil.inflate(
                layoutInflater,
                R.layout.fragment_cart,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holderMyCart: MyCartViewHolder, position: Int) {
        val item = values[position]
        holderMyCart.bind(item,position, callback)
    }

    override fun getItemCount(): Int = values.size

}