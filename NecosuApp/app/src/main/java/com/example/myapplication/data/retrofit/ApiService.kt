package com.example.myapplication.data.retrofit

import com.example.myapplication.data.response.ProductResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("product")
    fun getAllProduct(
    ): Call<ProductResponse>
}
