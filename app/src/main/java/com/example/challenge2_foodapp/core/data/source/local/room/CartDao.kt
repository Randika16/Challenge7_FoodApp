package com.example.challenge2_foodapp.core.data.source.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.challenge2_foodapp.core.data.source.local.entity.CartEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIntoCart(cart: CartEntity) : Long

    @Delete
    fun deleteFromCart(cart: CartEntity) : Int

    @Query("DELETE FROM cart_entity WHERE cart_id = :cartId")
    fun deleteCartByID(cartId: Int) : Int

    @Query("UPDATE cart_entity SET cart_food_quantity = :cartFoodQuantity - 1 WHERE cart_id = :cartId")
    fun decreaseCartItemQuantity(cartFoodQuantity: Int, cartId: Int) : Int

    @Query("UPDATE cart_entity SET cart_food_quantity = :cartFoodQuantity + 1 WHERE cart_id = :cartId")
    fun increaseCartItemQuantity(cartFoodQuantity: Int, cartId: Int) : Int

    @Query("UPDATE cart_entity SET cart_food_note = :note WHERE cart_id = :cartId")
    fun changeNoteForCartItem(cartId: Int, note: String) : Int

    @Update
    fun updateCart(cart: CartEntity) : Int

    @Query("SELECT * FROM cart_entity")
    fun getAllCartItem(): Flow<List<CartEntity>>

    @Query("SELECT * FROM cart_entity WHERE cart_id = :cartId")
    fun getSpecificCartItem(cartId: Int): Flow<CartEntity>

    @Query("DELETE FROM cart_entity")
    suspend fun deleteAll()
}