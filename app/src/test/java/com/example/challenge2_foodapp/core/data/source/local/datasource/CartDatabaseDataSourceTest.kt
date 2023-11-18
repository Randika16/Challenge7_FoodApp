package com.example.challenge2_foodapp.core.data.source.local.datasource

import app.cash.turbine.test
import com.example.challenge2_foodapp.core.data.source.local.entity.CartEntity
import com.example.challenge2_foodapp.core.data.source.local.room.CartDao
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class CartDatabaseDataSourceTest {

    @MockK
    lateinit var cartDao: CartDao

    private lateinit var cartDataSource: CartDataSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        cartDataSource = CartDatabaseDataSource(cartDao)
    }

    @Test
    fun insertIntoCart() {
        runTest {
            val mockCartEntity = mockk<CartEntity>()
            coEvery { cartDao.insertIntoCart(any()) } returns 1
            val result = cartDataSource.insertIntoCart(mockCartEntity)
            coVerify { cartDao.insertIntoCart(any()) }
            assertEquals(result, 1)
        }
    }

    @Test
    fun deleteFromCart() {
        runTest {
            val mockCartEntity = mockk<CartEntity>()
            coEvery { cartDao.deleteFromCart(any()) } returns 1
            val result = cartDataSource.deleteFromCart(mockCartEntity)
            coVerify { cartDao.deleteFromCart(any()) }
            assertEquals(result, 1)
        }
    }

    @Test
    fun deleteCartByID() {
        runTest {
            coEvery { cartDao.deleteCartByID(any()) } returns 1
            val result = cartDataSource.deleteCartByID(1)
            coVerify { cartDao.deleteCartByID(any()) }
            assertEquals(result, 1)
        }
    }

    @Test
    fun decreaseCartItemQuantity() {
        runTest {
            coEvery { cartDao.decreaseCartItemQuantity(any(), any()) } returns 1
            val result = cartDataSource.decreaseCartItemQuantity(1, 1)
            coVerify { cartDao.decreaseCartItemQuantity(any(), any()) }
            assertEquals(result, 1)
        }
    }

    @Test
    fun increaseCartItemQuantity() {
        runTest {
            coEvery { cartDao.increaseCartItemQuantity(any(), any()) } returns 1
            val result = cartDataSource.increaseCartItemQuantity(1, 1)
            coVerify { cartDao.increaseCartItemQuantity(any(), any()) }
            assertEquals(result, 1)
        }
    }

    @Test
    fun changeNoteForCartItem() {
        runTest {
            coEvery { cartDao.changeNoteForCartItem(any(), any()) } returns 1
            val result = cartDataSource.changeNoteForCartItem(1, "note")
            coVerify { cartDao.changeNoteForCartItem(any(), any()) }
            assertEquals(result, 1)
        }
    }

    @Test
    fun updateCart() {
        runTest {
            val mockCartEntity = mockk<CartEntity>()
            coEvery { cartDao.updateCart(any()) } returns 1
            val result = cartDataSource.updateCart(mockCartEntity)
            coVerify { cartDao.updateCart(any()) }
            assertEquals(result, 1)
        }
    }

    @Test
    fun getAllCartItem() {
        val itemEntityMock1 = mockk<CartEntity>()
        val itemEntityMock2 = mockk<CartEntity>()
        val listEntityMock = listOf(itemEntityMock1, itemEntityMock2)
        val flowMock = flow {
            emit(listEntityMock)
        }
        coEvery { cartDao.getAllCartItem() } returns flowMock
        runTest {
            cartDataSource.getAllCartItem().test {
                val result = awaitItem()
                assertEquals(listEntityMock, result)
                assertEquals(listEntityMock.size, result.size)
                assertEquals(itemEntityMock1, result[0])
                assertEquals(itemEntityMock2, result[1])
                awaitComplete()
            }
        }
    }

    @Test
    fun deleteAll() {
        runTest {
            coEvery { cartDao.deleteAll() } returns Unit
            val result = cartDataSource.deleteAll()
            coVerify { cartDao.deleteAll() }
            assertEquals(result, Unit)
        }
    }
}