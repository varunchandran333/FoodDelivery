package com.example.fooddelivery.ui.main.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.data.database.entity.Product
import com.example.fooddelivery.R
import com.example.fooddelivery.ui.main.utils.ProjectEventListeners

class CollapsingRecyclerViewDataAdapter(
    private val viewItemList: List<Product>,
    private val callback: ProjectEventListeners.ItemEvents
) :
    RecyclerView.Adapter<CollapsingRecyclerViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CollapsingRecyclerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return CollapsingRecyclerViewHolder(
            DataBindingUtil.inflate(
                layoutInflater,
                R.layout.layout_recycler_view_item,
                parent,
                false
            )
        )
    }

    fun updateButton() {

    }

    override fun onBindViewHolder(holder: CollapsingRecyclerViewHolder, position: Int) {
        // Get item dto in list.
        val viewItem = viewItemList[position]
        holder.bind(viewItem,callback)
    }

    override fun getItemCount(): Int {
        return viewItemList.size
    }
}