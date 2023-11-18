package com.example.challenge2_foodapp.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Food(
    val id: Int? = null,
    val name: String,
    val price: Int,
    val priceFormat: String,
    val description: String,
    val location: String,
    val image: String,
) : Parcelable