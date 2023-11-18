package com.example.challenge2_foodapp.core.domain.model

import android.os.Parcelable
import com.example.challenge2_foodapp.core.data.source.remote.response.category.CategoryResponse
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    val name: String,
    val image: String
) : Parcelable

fun CategoryResponse.toCategory() = Category(
    name = this.nama.orEmpty(),
    image = this.image_url.orEmpty()
)

fun Collection<CategoryResponse>.toCategoryList() = this.map {
    it.toCategory()
}
