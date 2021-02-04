package com.example.fooddelivery.ui.main.ui.main

import android.annotation.SuppressLint
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.data.database.entity.Product
import com.example.fooddelivery.R
import com.example.fooddelivery.databinding.LayoutRecyclerViewItemBinding
import com.example.fooddelivery.ui.main.utils.GlideApp
import com.example.fooddelivery.ui.main.utils.ProjectEventListeners
import com.example.fooddelivery.ui.main.utils.withDelay
import kotlinx.android.synthetic.main.layout_recycler_view_item.view.*

class CollapsingRecyclerViewHolder(private val binding: LayoutRecyclerViewItemBinding) :
    RecyclerView.ViewHolder(
        binding.root
    ) {
    @SuppressLint("SetTextI18n")
    fun bind(product: Product,
             callback: ProjectEventListeners.ItemEvents) {
        binding.product = product
        binding.root.buttonAddToCart.setOnClickListener {
            callback.onAddedToCart(product)
            binding.root.buttonAddToCart.text = binding.root.context.getString(R.string.added)
            binding.root.buttonAddToCart.setBackgroundColor(
                ContextCompat.getColor(
                    binding.root.context,
                    R.color.green
                )
            )
            withDelay(500) {
                binding.root.buttonAddToCart.text = "${product.sellingPrice} INR"
                binding.root.buttonAddToCart.setBackgroundColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.black
                    )
                )
            }
        }
    }

}