package com.example.challenge2_foodapp.core.data.source.remote.datasource

import com.example.challenge2_foodapp.core.data.source.remote.network.APIService
import com.example.challenge2_foodapp.core.data.source.remote.response.category.ListCategoryResponse
import com.example.challenge2_foodapp.core.data.source.remote.response.food.ListFoodResponse
import com.example.challenge2_foodapp.core.data.source.remote.response.order.OrderRequest
import com.example.challenge2_foodapp.core.data.source.remote.response.order.OrderResponse

interface RemoteDataSource {
    suspend fun getFoods(category: String? = null): ListFoodResponse
    suspend fun getCategories(): ListCategoryResponse
    suspend fun createOrder(orderRequest: OrderRequest): OrderResponse
}

class RemoteAPIDataSource(private val service: APIService) : RemoteDataSource {
    override suspend fun getFoods(category: String?): ListFoodResponse {
        return service.getFoods(category)
    }

    override suspend fun getCategories(): ListCategoryResponse {
        return service.getCategories()
    }

    override suspend fun createOrder(orderRequest: OrderRequest): OrderResponse {
        return service.order(orderRequest)
    }
}