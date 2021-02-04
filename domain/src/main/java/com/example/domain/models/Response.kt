package com.example.domain.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Response(
    @SerializedName("products")
    @Expose
    var products: List<Any>? = null,

    @SerializedName("result")
    @Expose
    var result: String? = null
)