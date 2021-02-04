package com.example.fooddelivery.ui.main.ui.cart

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.data.database.entity.Product
import com.example.fooddelivery.databinding.FragmentCartBinding
import com.example.fooddelivery.ui.main.utils.GlideApp
import com.example.fooddelivery.ui.main.utils.ProjectEventListeners
import kotlinx.android.synthetic.main.fragment_cart.view.*

class MyCartViewHolder(private val binding: FragmentCartBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(
        product: Product,
        position: Int,
        callback: ProjectEventListeners.CartEvents
    ) {
        binding.product = product
        binding.root.imageViewClose.setOnClickListener {
            callback.onRemoveItem(position)
        }
    }

    override fun toString(): String {
        return super.toString() + " '" + "'"
    }
}

@BindingAdapter("imageUrl")
fun showImage(view: ImageView, imageUrl: Int) {
    GlideApp.with(view.context)
        .load(imageUrl)
        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        .into(view)
}