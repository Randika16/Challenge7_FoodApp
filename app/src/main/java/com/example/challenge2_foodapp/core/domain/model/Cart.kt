package com.example.challenge2_foodapp.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Cart(
    var id: Int = 0,
    var foodItem: Food,
    var foodQuantity: Int = 0,
    var foodNote: String? = null
) : Parcelable
