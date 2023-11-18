package com.example.challenge2_foodapp.core.data.source.remote.network

import com.example.challenge2_foodapp.BuildConfig
import com.example.challenge2_foodapp.core.data.source.remote.response.category.ListCategoryResponse
import com.example.challenge2_foodapp.core.data.source.remote.response.food.ListFoodResponse
import com.example.challenge2_foodapp.core.data.source.remote.response.order.OrderRequest
import com.example.challenge2_foodapp.core.data.source.remote.response.order.OrderResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface APIService {
    @GET("listmenu")
    suspend fun getFoods(
        @Query("c") category: String? = null
    ): ListFoodResponse

    @GET("category")
    suspend fun getCategories(): ListCategoryResponse

    @POST("order")
    suspend fun order(
        @Body body: OrderRequest
    ): OrderResponse

    companion object {
        @JvmStatic
        operator fun invoke(): APIService {
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
            return retrofit.create(APIService::class.java)
        }
    }
}