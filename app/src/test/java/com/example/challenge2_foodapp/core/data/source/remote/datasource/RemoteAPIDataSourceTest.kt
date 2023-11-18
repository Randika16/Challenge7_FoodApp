package com.example.challenge2_foodapp.core.data.source.remote.datasource

import com.example.challenge2_foodapp.core.data.source.remote.network.APIService
import com.example.challenge2_foodapp.core.data.source.remote.response.category.ListCategoryResponse
import com.example.challenge2_foodapp.core.data.source.remote.response.food.ListFoodResponse
import com.example.challenge2_foodapp.core.data.source.remote.response.order.OrderRequest
import com.example.challenge2_foodapp.core.data.source.remote.response.order.OrderResponse
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class RemoteAPIDataSourceTest {

    @MockK
    lateinit var service: APIService

    private lateinit var remoteDataSource: RemoteDataSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        remoteDataSource = RemoteAPIDataSource(service)
    }

    @Test
    fun getFoods() {
        runTest {
            val mockResponse = mockk<ListFoodResponse>()
            coEvery { service.getFoods(any()) } returns mockResponse
            val response = remoteDataSource.getFoods("mie")
            coVerify { service.getFoods(any()) }
            assertEquals(response, mockResponse)
        }
    }

    @Test
    fun getCategories() {
        runTest {
            val mockResponse = mockk<ListCategoryResponse>(relaxed = true)
            coEvery { service.getCategories() } returns mockResponse
            val response = remoteDataSource.getCategories()
            coVerify { service.getCategories() }
            assertEquals(response, mockResponse)
        }
    }

    @Test
    fun createOrder() {
        runTest {
            val mockResponse = mockk<OrderResponse>(relaxed = true)
            val mockRequest = mockk<OrderRequest>(relaxed = true)
            coEvery { service.order(any()) } returns mockResponse
            val response = remoteDataSource.createOrder(mockRequest)
            coVerify { service.order(any()) }
            assertEquals(response, mockResponse)
        }
    }
}