package com.example.fooddelivery.ui.main.utils

import com.example.data.database.entity.Product


interface ProjectEventListeners {

    interface ItemEvents {
        fun onAddedToCart(product: Product)
    }
    interface CartEvents {
        fun onRemoveItem(position: Int)
    }
}

