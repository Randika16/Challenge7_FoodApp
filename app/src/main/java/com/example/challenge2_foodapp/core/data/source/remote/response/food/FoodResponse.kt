package com.example.challenge2_foodapp.core.data.source.remote.response.food

import com.example.challenge2_foodapp.core.domain.model.Food
import com.google.errorprone.annotations.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class FoodResponse(
    @SerializedName("image_url")
    val image_url: String,
    @SerializedName("nama")
    val nama: String,
    @SerializedName("harga_format")
    val harga_format: String,
    @SerializedName("harga")
    val harga: Int,
    @SerializedName("detail")
    val detail: String,
    @SerializedName("alamat_resto")
    val alamat_resto: String,
)

fun FoodResponse.toFood() = Food(
    id = 0,
    name = this.nama.orEmpty(),
    price = this.harga,
    priceFormat = this.harga_format,
    description = this.detail.orEmpty(),
    location = this.alamat_resto.orEmpty(),
    image = this.image_url.orEmpty()
)

fun Collection<FoodResponse>.toFoodList() = this.map { it.toFood() }