package com.example.challenge2_foodapp.core.utils

import com.example.challenge2_foodapp.core.data.source.local.entity.CartEntity
import com.example.challenge2_foodapp.core.data.source.local.entity.FoodEntity
import com.example.challenge2_foodapp.core.data.source.remote.response.category.CategoryResponse
import com.example.challenge2_foodapp.core.data.source.remote.response.food.FoodResponse
import com.example.challenge2_foodapp.core.domain.model.Cart
import com.example.challenge2_foodapp.core.domain.model.Category
import com.example.challenge2_foodapp.core.domain.model.Food

object DataMapper {

    fun mapFoodResponsesToEntities(input: List<FoodResponse>): List<FoodEntity> {
        val foodList = ArrayList<FoodEntity>()
        input.map {
            val food = FoodEntity(
                image = it.image_url,
                name = it.nama,
                priceFormat = it.harga_format,
                price = it.harga,
                description = it.detail,
                location = it.alamat_resto,
            )
            foodList.add(food)
        }
        return foodList
    }

    fun mapFoodDomainToEntity(input: Food): FoodEntity {
        return FoodEntity(
            image = input.image,
            name = input.name,
            priceFormat = input.priceFormat,
            price = input.price,
            description = input.description,
            location = input.location,
        )
    }

    fun mapFoodEntityToDomain(input: FoodEntity): Food {
        return Food(
            image = input.image,
            name = input.name,
            priceFormat = input.priceFormat,
            price = input.price,
            description = input.description,
            location = input.location,
        )
    }

    fun mapFoodEntitiesToDomain(input: List<FoodEntity>): List<Food> =
        input.map {
            Food(
                image = it.image,
                name = it.name,
                priceFormat = it.priceFormat,
                price = it.price,
                description = it.description,
                location = it.location,
            )
        }

    fun mapCategoryResponsesToDomain(input: List<CategoryResponse>): List<Category> {
        val categoryList = ArrayList<Category>()
        input.map {
            val category = Category(
                image = it.image_url,
                name = it.nama,
            )
            categoryList.add(category)
        }
        return categoryList
    }

    fun mapCartDomainToEntity(input: Cart): CartEntity {
        return CartEntity(
            id = input.id,
            foodItem = mapFoodDomainToEntity(input.foodItem),
            foodQuantity = input.foodQuantity,
            foodNote = input.foodNote,
        )
    }

    fun mapCartListEntityToDomain(input: List<CartEntity>): List<Cart> =
        input.map {
            Cart(
                id = it.id,
                foodItem = mapFoodEntityToDomain(it.foodItem),
                foodQuantity = it.foodQuantity,
                foodNote = it.foodNote,
            )
        }
}