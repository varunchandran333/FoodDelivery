package com.example.domain.models

import com.google.gson.annotations.SerializedName

class ApiErrorResponse {
    @JvmField
    @SerializedName("reason")
    var reason = "failed"

    @SerializedName("result")
    var result = "error"

    @JvmField
    @SerializedName("code")
    var code = 1000
    override fun toString(): String {
        return "{" +
                "reason='" + reason + '\'' +
                ", result='" + result + '\'' +
                ", code=" + code +
                '}'
    }

    companion object {
        const val NETWORK_FAILURE = 1001
    }
}