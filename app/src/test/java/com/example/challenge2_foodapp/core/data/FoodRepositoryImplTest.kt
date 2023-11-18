package com.example.challenge2_foodapp.core.data

import app.cash.turbine.test
import com.example.challenge2_foodapp.core.data.source.remote.datasource.RemoteDataSource
import com.example.challenge2_foodapp.core.data.source.remote.response.category.CategoryResponse
import com.example.challenge2_foodapp.core.data.source.remote.response.category.ListCategoryResponse
import com.example.challenge2_foodapp.core.data.source.remote.response.food.FoodResponse
import com.example.challenge2_foodapp.core.data.source.remote.response.food.ListFoodResponse
import com.example.challenge2_foodapp.core.utils.ResultWrapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


class FoodRepositoryImplTest {

    @MockK
    lateinit var remoteDataSource: RemoteDataSource

    private lateinit var repository: FoodRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = FoodRepositoryImpl(remoteDataSource)
    }

    @Test
    fun `get category when loading`() {
        val mockCategoryResponse = mockk<ListCategoryResponse>()
        runTest {
            coEvery { remoteDataSource.getCategories() } returns mockCategoryResponse
            repository.getCategories().map {
                delay(100)
                it
            }.test {
                delay(110)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Loading)
                coVerify { remoteDataSource.getCategories() }
            }
        }
    }

    @Test
    fun `get categories when success`() {
        val fakeCategoryResponse = CategoryResponse(
            image_url = "https://d1vbn70lmn1nqe.cloudfront.net/prod/wp-content/uploads/2023/07/13024827/ini-cara-membuat-mie-goreng-yang-lebih-sehat-dengan-bahan-sederhana-halodoc.jpg.webp",
            nama = "Mie"
        )

        val fakeCategoriesResponse = ListCategoryResponse(
            code = 200,
            status = true,
            message = "Success",
            data = listOf(fakeCategoryResponse)
        )

        runTest {
            coEvery { remoteDataSource.getCategories() } returns fakeCategoriesResponse
            repository.getCategories().map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Success)
                assertEquals(data.payload?.size, 1)
                assertEquals(data.payload?.get(0)?.name, "Mie")
                coVerify { remoteDataSource.getCategories() }
            }
        }
    }

    @Test
    fun `get category when empty`() {
        val fakeCategoriesResponse = ListCategoryResponse(
            code = 200,
            status = true,
            message = "Success",
            data = emptyList()
        )
        runTest {
            coEvery { remoteDataSource.getCategories() } returns fakeCategoriesResponse
            repository.getCategories().map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Empty)
                coVerify { remoteDataSource.getCategories() }
            }
        }
    }

    @Test
    fun `get category when error`() {
        runTest {
            coEvery { remoteDataSource.getCategories() } throws IllegalStateException("Mock error")
            repository.getCategories().map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Error)
                coVerify { remoteDataSource.getCategories() }
            }
        }
    }

    @Test
    fun `get food when loading`() {
        val mockProductResponse = mockk<ListFoodResponse>()
        runTest {
            coEvery { remoteDataSource.getFoods(any()) } returns mockProductResponse
            repository.getFoods("mie").map {
                delay(100)
                it
            }.test {
                delay(110)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Loading)
                coVerify { remoteDataSource.getFoods(any()) }
            }
        }
    }

    @Test
    fun `get food when success`() {
        val fakeProductItemResponse = FoodResponse(
            image_url = "https://cdn-image.hipwee.com/wp-content/uploads/2019/06/hipwee-Depositphotos_295327104_xl-2015.jpg",
            nama = "Mie Dogdog",
            harga_format = "Rp. 5.000",
            harga = 5000,
            detail = "Olahan mi instan emang paling bisa menggugah selera. Salah satu olahan mi instan yang banyak diminati adalah mi dog-dog.",
            alamat_resto = "Jl. BSD Green Office Park Jl. BSD Grand Boulevard, Sampora, BSD, Kabupaten Tangerang, Banten 15345"
        )
        val fakeProductsResponse = ListFoodResponse(
            code = 200,
            status = true,
            message = "Success",
            data = listOf(fakeProductItemResponse)
        )
        runTest {
            coEvery { remoteDataSource.getFoods(any()) } returns fakeProductsResponse
            repository.getFoods("mie").map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Success)
                assertEquals(data.payload?.size, 1)
                assertEquals(data.payload?.get(0)?.name, "Mie Dogdog")
                coVerify { remoteDataSource.getFoods(any()) }
            }
        }
    }

    @Test
    fun `get food when empty`() {
        val fakeProductsResponse = ListFoodResponse(
            code = 200,
            status = true,
            message = "Success",
            data = emptyList()
        )
        runTest {
            coEvery { remoteDataSource.getFoods(any()) } returns fakeProductsResponse
            repository.getFoods().map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Empty)
                coVerify { remoteDataSource.getFoods(any()) }
            }
        }
    }

    @Test
    fun `get food when error`() {
        runTest {
            coEvery { remoteDataSource.getFoods(any()) } throws IllegalStateException("Mock error")
            repository.getFoods("bebas").map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Error)
                coVerify { remoteDataSource.getFoods(any()) }
            }
        }
    }


}