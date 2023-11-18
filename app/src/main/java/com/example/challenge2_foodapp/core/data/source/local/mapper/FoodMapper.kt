package com.example.challenge2_foodapp.core.data.source.local.mapper

import com.example.challenge2_foodapp.core.data.source.local.entity.FoodEntity
import com.example.challenge2_foodapp.core.domain.model.Food

fun FoodEntity?.toFood() = Food(
    name = this?.name ?: "",
    price = this?.price ?: 0,
    priceFormat = this?.priceFormat ?: "",
    description = this?.description ?: "",
    location = this?.location ?: "",
    image = this?.image ?: ""
)

fun Food.toFoodEntity() = FoodEntity(
    name = this.name,
    price = this.price,
    priceFormat = this.priceFormat,
    description = this.description,
    location = this.location,
    image = this.image
)

fun List<FoodEntity?>.toFoodList() = this.map { it.toFood() }

fun List<Food>.toFoodEntityList() = this.map { it.toFoodEntity() }