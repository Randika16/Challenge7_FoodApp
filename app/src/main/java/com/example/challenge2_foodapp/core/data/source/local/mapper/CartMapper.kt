package com.example.challenge2_foodapp.core.data.source.local.mapper

import com.example.challenge2_foodapp.core.data.source.local.entity.CartEntity
import com.example.challenge2_foodapp.core.data.source.local.entity.FoodEntity
import com.example.challenge2_foodapp.core.domain.model.Cart
import com.example.challenge2_foodapp.core.domain.model.Food

fun CartEntity?.toCart() = Cart(
    id = this?.id ?: 0,
    foodItem = Food(
        id = this?.id,
        this?.foodItem?.name ?: "",
        this?.foodItem?.price ?: 0,
        this?.foodItem?.priceFormat ?: "",
        this?.foodItem?.description ?: "",
        this?.foodItem?.location ?: "",
        this?.foodItem?.image ?: ""
    ),
    foodQuantity = this?.foodQuantity ?: 0,
    foodNote = this?.foodNote ?: ""
)

fun Cart.toCartEntity() = CartEntity(
    id = this.id,
    foodItem = FoodEntity(
        name = this.foodItem.name,
        price = this.foodItem.price,
        priceFormat = this.foodItem.priceFormat,
        description = this.foodItem.description,
        location = this.foodItem.location,
        image = this.foodItem.image
    ),
    foodQuantity = this.foodQuantity,
    foodNote = this.foodNote
)

fun List<CartEntity?>.toCartList() = this.map { it.toCart() }

fun List<Cart>.toCartEntityList() = this.map { it.toCartEntity() }