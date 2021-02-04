package com.example.data.apiServices

import com.example.data.models.GetAllDataResponseModel
import com.example.fooddelivery.BuildConfig
import retrofit2.Response
import retrofit2.http.*

interface GetAllDataService {

    @Headers(
        "Content-Type: application/json",
        "Accept: application/json",
        "cache-control: no-cache"
    )
    @GET(BuildConfig.HOST)
    suspend fun getAllData(@Header("token") token: String?)
            : Response<GetAllDataResponseModel>
}