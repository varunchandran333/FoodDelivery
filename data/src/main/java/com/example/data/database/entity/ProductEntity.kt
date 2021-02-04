package com.example.data.database.entity

import androidx.room.ColumnInfo
import com.example.domain.models.Response
import com.example.data.base.DomainMapper

class ProductEntity(
    @ColumnInfo(name = "products")
    var products: List<Product>,

    @ColumnInfo(name = "result")
    var result: String? = null
) : DomainMapper<Response> {
    override fun mapToDomainModel() = Response(products, result)
}