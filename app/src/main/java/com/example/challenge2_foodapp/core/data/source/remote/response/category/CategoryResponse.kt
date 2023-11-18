package com.example.challenge2_foodapp.core.data.source.remote.response.category

import com.google.errorprone.annotations.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class CategoryResponse(
    @SerializedName("image_url")
    val image_url: String,
    @SerializedName("nama")
    val nama: String,
)
