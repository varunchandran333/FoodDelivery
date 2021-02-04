package com.example.fooddelivery.ui.main.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop


    @BindingAdapter("bind:imageUrl")
    fun loadImage(view: ImageView, imageUrl: Int) {
        GlideApp.with(view.context)
            .load(imageUrl)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .transform(CircleCrop())
            .into(view)
    }
