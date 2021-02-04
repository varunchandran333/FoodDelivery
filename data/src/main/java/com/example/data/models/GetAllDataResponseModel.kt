package com.example.data.models

import com.example.data.base.RoomMapper
import com.example.data.database.entity.Product
import com.example.data.database.entity.ProductEntity
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetAllDataResponseModel(
    @SerializedName("products")
    @Expose
    var products: List<Product>? = null,

    @SerializedName("result")
    @Expose
    var result: String? = null
) : RoomMapper<ProductEntity> {

    override fun mapToRoomEntity() = ProductEntity(
        products!!,
        result
    )
}