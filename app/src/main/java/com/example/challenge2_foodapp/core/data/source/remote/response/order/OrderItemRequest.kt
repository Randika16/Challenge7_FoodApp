package com.example.challenge2_foodapp.core.data.source.remote.response.order

data class OrderItemRequest(
    val nama: String,
    val qty: Int,
    val catatan: String? = null,
    val harga: Int
)
