package com.example.challenge2_foodapp.core.data

import app.cash.turbine.test
import com.example.challenge2_foodapp.core.data.source.local.datasource.CartDataSource
import com.example.challenge2_foodapp.core.data.source.local.entity.CartEntity
import com.example.challenge2_foodapp.core.data.source.local.entity.FoodEntity
import com.example.challenge2_foodapp.core.data.source.remote.datasource.RemoteDataSource
import com.example.challenge2_foodapp.core.data.source.remote.response.order.OrderResponse
import com.example.challenge2_foodapp.core.domain.model.Cart
import com.example.challenge2_foodapp.core.domain.model.Food
import com.example.challenge2_foodapp.core.utils.ResultWrapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


class CartRepositoryImplTest {

    @MockK
    lateinit var localDataSource: CartDataSource

    @MockK
    lateinit var remoteDataSource: RemoteDataSource

    private lateinit var repository: CartRepository

    private val fakeCartEntityList = listOf(
        CartEntity(
            id = 1,
            foodItem = FoodEntity(
                name = "Sate",
                price = 12000,
                priceFormat = "Rp 12.000",
                description = "Sate terenak di Jakarta",
                location = "Jakarta",
                image = "http://google.com"
            ),
            foodQuantity = 1,
            foodNote = "tidak pedas"
        ),
        CartEntity(
            id = 2,
            foodItem = FoodEntity(
                name = "Bakso",
                price = 15000,
                priceFormat = "Rp 10.000",
                description = "Bakso terenak di Jakarta",
                location = "Jakarta",
                image = "http://google.com"
            ),
            foodQuantity = 1,
            foodNote = "pedas"
        )
    )

    private val mockCartDomain = Cart(
        id = 1,
        foodItem = Food(
            name = "Sate",
            price = 12000,
            priceFormat = "Rp 12.000",
            description = "Sate terenak di Jakarta",
            location = "Jakarta",
            image = "http://google.com"
        ),
        foodQuantity = 1,
        foodNote = "tidak pedas"
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = CartRepositoryImpl(remoteDataSource, localDataSource)
    }

    @Test
    fun `get cart when loading`() {
        every { localDataSource.getAllCartItem() } returns flow {
            emit(fakeCartEntityList)
        }
        runTest {
            repository.getCart().map {
                delay(100)
                it
            }.test {
                delay(2101)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Loading)
                verify { localDataSource.getAllCartItem() }
            }
        }
    }

    @Test
    fun `get cart when error`() {
        every { localDataSource.getAllCartItem() } returns flow {
            throw IllegalStateException("Mock Error")
        }
        runTest {
            repository.getCart().map {
                delay(100)
                it
            }.test {
                delay(4000)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Error)
                verify { localDataSource.getAllCartItem() }
            }
        }
    }

    @Test
    fun `get cart when empty`() {
        every { localDataSource.getAllCartItem() } returns flow {
            emit(listOf())
        }
        runTest {
            repository.getCart().map {
                delay(100)
                it
            }.test {
                delay(2201)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Empty)
                verify { localDataSource.getAllCartItem() }
            }
        }
    }

    @Test
    fun `get cart when success`() {
        every { localDataSource.getAllCartItem() } returns flow {
            emit(fakeCartEntityList)
        }
        runTest {
            repository.getCart().map {
                delay(100)
                it
            }.test {
                delay(2201)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Success)
                assertEquals(data.payload?.first?.size, 2)
                assertEquals(data.payload?.second, 27000)
                verify { localDataSource.getAllCartItem() }
            }
        }
    }

    @Test
    fun `decrease cart item when quantity more than 0`() {
        val mockCart = Cart(
            id = 1,
            foodItem = Food(
                name = "Sate",
                price = 12000,
                priceFormat = "Rp 12.000",
                description = "Sate terenak di Jakarta",
                location = "Jakarta",
                image = "http://google.com"
            ),
            foodQuantity = 2,
            foodNote = "tidak pedas"
        )

        coEvery { localDataSource.deleteFromCart(any()) } returns 1
        coEvery { localDataSource.updateCart(any()) } returns 1
        runTest {
            repository.decreaseCart(mockCart).map {
                delay(100)
                it
            }.test {
                delay(210)
                val result = expectMostRecentItem()
                assertEquals(result.payload, true)
                coVerify(atLeast = 0) { localDataSource.deleteFromCart(any()) }
                coVerify(atLeast = 1) { localDataSource.updateCart(any()) }
            }
        }
    }

    @Test
    fun `create cart when loading`() {
        runTest {
            val mockProduct = mockk<Food>(relaxed = true)
            coEvery { localDataSource.insertIntoCart(any()) } returns 1
            repository.insertIntoCart(mockProduct, 1)
                .map {
                    delay(100)
                    it
                }.test {
                    delay(110)
                    val result = expectMostRecentItem()
                    assertTrue(result is ResultWrapper.Loading)
                    coVerify { localDataSource.insertIntoCart(any()) }
                }
        }
    }

    @Test
    fun `create cart when success`() {
        runTest {
            val mockProduct = mockk<Food>(relaxed = true)
            coEvery { localDataSource.insertIntoCart(any()) } returns 1
            repository.insertIntoCart(mockProduct, 1)
                .map {
                    delay(100)
                    it
                }.test {
                    delay(210)
                    val result = expectMostRecentItem()
                    assertTrue(result is ResultWrapper.Success)
                    assertEquals(result.payload, true)
                    coVerify { localDataSource.insertIntoCart(any()) }
                }
        }
    }

    @Test
    fun `create cart when error, product id not null`() {
        runTest {
            val mockProduct = mockk<Food>(relaxed = true)
            coEvery { localDataSource.insertIntoCart(any()) } throws IllegalStateException("Mock Error")
            repository.insertIntoCart(mockProduct, 1)
                .map {
                    delay(100)
                    it
                }.test {
                    delay(210)
                    val result = expectMostRecentItem()
                    assertTrue(result is ResultWrapper.Error)
                    coVerify { localDataSource.insertIntoCart(any()) }
                }
        }
    }

    @Test
    fun `create cart when error, product id null`() {
        runTest {
            val mockProduct = mockk<Food>(relaxed = true) {
                every { id } returns null
            }
            coEvery { localDataSource.insertIntoCart(any()) } returns 1
            repository.insertIntoCart(mockProduct, 1)
                .map {
                    delay(100)
                    it
                }.test {
                    delay(210)
                    val result = expectMostRecentItem()
                    assertTrue(result is ResultWrapper.Error)
                    assertEquals(result.exception?.message, "Product ID not found")
                    coVerify(atLeast = 0) { localDataSource.insertIntoCart(any()) }
                }
        }
    }

    @Test
    fun `increase cart item`() {
        coEvery { localDataSource.updateCart(any()) } returns 1
        runTest {
            repository.increaseCart(mockCartDomain).map {
                delay(100)
                it
            }.test {
                delay(210)
                val result = expectMostRecentItem()
                assertEquals(result.payload, true)
                coVerify(atLeast = 1) { localDataSource.updateCart(any()) }
            }
        }
    }

    @Test
    fun `decrease cart item when quantity less or equal 0`() {
        val mockCart = Cart(
            id = 1,
            foodItem = Food(
                name = "Sate",
                price = 12000,
                priceFormat = "Rp 12.000",
                description = "Sate terenak di Jakarta",
                location = "Jakarta",
                image = "http://google.com"
            ),
            foodQuantity = 1,
            foodNote = "tidak pedas"
        )

        coEvery { localDataSource.deleteFromCart(any()) } returns 1
        coEvery { localDataSource.updateCart(any()) } returns 1
        runTest {
            repository.decreaseCart(mockCart).map {
                delay(100)
                it
            }.test {
                delay(210)
                val result = expectMostRecentItem()
                assertEquals(result.payload, true)
                coVerify(atLeast = 1) { localDataSource.deleteFromCart(any()) }
                coVerify(atLeast = 0) { localDataSource.updateCart(any()) }
            }
        }
    }


    @Test
    fun `insert cart notes`() {
        coEvery { localDataSource.updateCart(any()) } returns 1
        runTest {
            repository.setCartNotes(mockCartDomain).map {
                delay(100)
                it
            }.test {
                delay(210)
                val result = expectMostRecentItem()
                assertEquals(result.payload, true)
                coVerify(atLeast = 1) { localDataSource.updateCart(any()) }
            }
        }
    }

    @Test
    fun `delete cart`() {
        coEvery { localDataSource.deleteFromCart(any()) } returns 1
        runTest {
            repository.deleteCart(mockCartDomain).map {
                delay(100)
                it
            }.test {
                delay(210)
                val result = expectMostRecentItem()
                assertEquals(result.payload, true)
                coVerify(atLeast = 1) { localDataSource.deleteFromCart(any()) }
            }
        }
    }

    @Test
    fun `order food`() {
        runTest {
            val mockCarts = listOf(
                Cart(
                    id = 1,
                    foodItem = Food(
                        name = "Sate",
                        price = 12000,
                        priceFormat = "Rp 12.000",
                        description = "Sate terenak di Jakarta",
                        location = "Jakarta",
                        image = "http://google.com"
                    ),
                    foodQuantity = 1,
                    foodNote = "tidak pedas"
                ),
                Cart(
                    id = 2,
                    foodItem = Food(
                        name = "Bakso",
                        price = 10000,
                        priceFormat = "Rp 10.000",
                        description = "Bakso terenak di Jakarta",
                        location = "Jakarta",
                        image = "http://google.com"
                    ),
                    foodQuantity = 1,
                    foodNote = "pedas"
                )
            )

            coEvery { remoteDataSource.createOrder(any()) } returns OrderResponse(
                code = 200,
                message = "Success",
                status = true
            )

            repository.order(mockCarts).map {
                delay(100)
                it
            }.test {
                delay(210)
                val result = expectMostRecentItem()
                assertTrue(result is ResultWrapper.Success)
            }
        }
    }

    @Test
    fun `delete all cart`() {
        coEvery { localDataSource.deleteAll() } returns Unit
        runTest {
            val result = repository.deleteAll()
            coVerify { localDataSource.deleteAll() }
            assertEquals(result, Unit)
        }
    }

}