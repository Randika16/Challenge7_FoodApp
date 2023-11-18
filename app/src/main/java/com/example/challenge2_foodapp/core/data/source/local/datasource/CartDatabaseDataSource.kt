package com.example.challenge2_foodapp.core.data.source.local.datasource

import com.example.challenge2_foodapp.core.data.source.local.entity.CartEntity
import com.example.challenge2_foodapp.core.data.source.local.room.CartDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

interface CartDataSource {
    fun insertIntoCart(cart: CartEntity) : Long
    fun deleteFromCart(cart: CartEntity) : Int
    fun deleteCartByID(cartId: Int) : Int
    fun decreaseCartItemQuantity(cartFoodQuantity: Int, cartId: Int) : Int
    fun increaseCartItemQuantity(cartFoodQuantity: Int, cartId: Int) : Int
    fun changeNoteForCartItem(cartId: Int, note: String) : Int
    fun updateCart(cart: CartEntity) : Int
    fun getAllCartItem(): Flow<List<CartEntity>>
    suspend fun deleteAll()
}

class CartDatabaseDataSource(private val cartDao: CartDao) : CartDataSource {

    override fun insertIntoCart(cart: CartEntity): Long {
        return cartDao.insertIntoCart(cart)
    }

    override fun deleteFromCart(cart: CartEntity): Int {
        return cartDao.deleteFromCart(cart)
    }

    override fun deleteCartByID(cartId: Int): Int {
        return cartDao.deleteCartByID(cartId)
    }

    override fun decreaseCartItemQuantity(cartFoodQuantity: Int, cartId: Int): Int {
        return cartDao.decreaseCartItemQuantity(cartFoodQuantity, cartId)
    }

    override fun increaseCartItemQuantity(cartFoodQuantity: Int, cartId: Int): Int {
        return cartDao.increaseCartItemQuantity(cartFoodQuantity, cartId)
    }

    override fun changeNoteForCartItem(cartId: Int, note: String): Int {
        return cartDao.changeNoteForCartItem(cartId, note)
    }

    override fun updateCart(cart: CartEntity): Int {
        return cartDao.updateCart(cart)
    }

    override fun getAllCartItem(): Flow<List<CartEntity>> {
        return cartDao.getAllCartItem()
    }

    override suspend fun deleteAll() {
        cartDao.deleteAll()
    }
}