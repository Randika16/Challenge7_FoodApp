package com.example.challenge2_foodapp.core.data

import com.example.challenge2_foodapp.core.data.source.local.datasource.CartDataSource
import com.example.challenge2_foodapp.core.data.source.local.entity.CartEntity
import com.example.challenge2_foodapp.core.data.source.local.mapper.toCartEntity
import com.example.challenge2_foodapp.core.data.source.local.mapper.toCartList
import com.example.challenge2_foodapp.core.data.source.local.mapper.toFoodEntity
import com.example.challenge2_foodapp.core.data.source.remote.datasource.RemoteDataSource
import com.example.challenge2_foodapp.core.data.source.remote.response.order.OrderItemRequest
import com.example.challenge2_foodapp.core.data.source.remote.response.order.OrderRequest
import com.example.challenge2_foodapp.core.domain.model.Cart
import com.example.challenge2_foodapp.core.domain.model.Food
import com.example.challenge2_foodapp.core.utils.ResultWrapper
import com.example.challenge2_foodapp.core.utils.proceed
import com.example.challenge2_foodapp.core.utils.proceedFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart


interface CartRepository {
    fun getCart(): Flow<ResultWrapper<Pair<List<Cart>, Int>>>
    suspend fun insertIntoCart(food: Food, totalQuantity: Int): Flow<ResultWrapper<Boolean>>
    suspend fun decreaseCart(item: Cart): Flow<ResultWrapper<Boolean>>
    suspend fun increaseCart(item: Cart): Flow<ResultWrapper<Boolean>>
    suspend fun setCartNotes(item: Cart): Flow<ResultWrapper<Boolean>>
    suspend fun deleteCart(item: Cart): Flow<ResultWrapper<Boolean>>
    suspend fun order(items: List<Cart>): Flow<ResultWrapper<Boolean>>
    suspend fun deleteAll()
}

class CartRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: CartDataSource,
) : CartRepository {

    override fun getCart(): Flow<ResultWrapper<Pair<List<Cart>, Int>>> {
        return localDataSource.getAllCartItem()
            .map {
                proceed {
                    val result = it.toCartList()
                    val totalPrice = result.sumOf {
                        val pricePerItem = it.foodItem.price
                        val quantity = it.foodQuantity
                        pricePerItem * quantity
                    }
                    Pair(result, totalPrice)
                }
            }.map {
                if (it.payload?.first?.isEmpty() == true) {
                    ResultWrapper.Empty(it.payload)
                } else {
                    it
                }
            }.catch {
                emit(ResultWrapper.Error(Exception(it)))
            }
            .onStart {
                emit(ResultWrapper.Loading())
                delay(2000)
            }
    }

    override suspend fun insertIntoCart(
        food: Food,
        totalQuantity: Int
    ): Flow<ResultWrapper<Boolean>> {
        return food.id?.let { productId ->
            proceedFlow {
                val affectedRow = localDataSource.insertIntoCart(
                    CartEntity(
                        id = productId,
                        foodItem = food.toFoodEntity(),
                        foodQuantity = totalQuantity,
                        foodNote = null
                    )
                )
                affectedRow > 0
            }
        } ?: flow {
            emit(ResultWrapper.Error(IllegalStateException("Product ID not found")))
        }
    }

    override suspend fun decreaseCart(item: Cart): Flow<ResultWrapper<Boolean>> {
        val modifiedCart = item.copy().apply {
            foodQuantity -= 1
        }

        return if (modifiedCart.foodQuantity <= 0) {
            proceedFlow { localDataSource.deleteFromCart(modifiedCart.toCartEntity()) > 0 }
        } else {
            proceedFlow { localDataSource.updateCart(modifiedCart.toCartEntity()) > 0 }
        }
    }

    override suspend fun increaseCart(item: Cart): Flow<ResultWrapper<Boolean>> {
        val modifiedCart = item.copy().apply {
            foodQuantity += 1
        }
        return proceedFlow { localDataSource.updateCart(modifiedCart.toCartEntity()) > 0 }
    }

    override suspend fun setCartNotes(item: Cart): Flow<ResultWrapper<Boolean>> {
        return proceedFlow { localDataSource.updateCart(item.toCartEntity()) > 0 }
    }

    override suspend fun deleteCart(item: Cart): Flow<ResultWrapper<Boolean>> {
        return proceedFlow { localDataSource.deleteFromCart(item.toCartEntity()) > 0 }
    }

    override suspend fun order(items: List<Cart>): Flow<ResultWrapper<Boolean>> {
        return proceedFlow {
            val orderItems = items.map {
                OrderItemRequest(
                    it.foodItem.name,
                    it.foodQuantity,
                    it.foodNote,
                    it.foodItem.price
                )
            }
            val orderRequest = OrderRequest(orderItems)
            remoteDataSource.createOrder(orderRequest).status
        }
    }

    override suspend fun deleteAll() {
        localDataSource.deleteAll()
    }

}