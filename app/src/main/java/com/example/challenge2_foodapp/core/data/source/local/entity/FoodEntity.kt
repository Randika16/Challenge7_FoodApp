package com.example.challenge2_foodapp.core.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity("food_entity")
data class FoodEntity(

    @PrimaryKey()
    @ColumnInfo(name = "food_name")
    val name: String,

    @ColumnInfo(name = "food_price")
    val price: Int,

    @ColumnInfo(name = "food_price_format")
    val priceFormat: String,

    @ColumnInfo(name = "food_description")
    val description: String,

    @ColumnInfo(name = "food_location")
    val location: String,

    @ColumnInfo(name = "food_image")
    val image: String,
) : Serializable