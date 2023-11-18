package com.example.challenge2_foodapp.core.data

import com.example.challenge2_foodapp.core.data.source.remote.datasource.RemoteDataSource
import com.example.challenge2_foodapp.core.data.source.remote.response.food.toFoodList
import com.example.challenge2_foodapp.core.domain.model.Category
import com.example.challenge2_foodapp.core.domain.model.Food
import com.example.challenge2_foodapp.core.domain.model.toCategoryList
import com.example.challenge2_foodapp.core.utils.ResultWrapper
import com.example.challenge2_foodapp.core.utils.proceedFlow
import kotlinx.coroutines.flow.Flow


interface FoodRepository {
    fun getCategories(): Flow<ResultWrapper<List<Category>>>
    fun getFoods(category: String? = null): Flow<ResultWrapper<List<Food>>>
}

class FoodRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
) : FoodRepository {
    override fun getCategories(): Flow<ResultWrapper<List<Category>>> {
        return proceedFlow {
            remoteDataSource.getCategories().data.toCategoryList() ?: emptyList()
        }
    }

    override fun getFoods(category: String?): Flow<ResultWrapper<List<Food>>> {
        return proceedFlow {
            remoteDataSource.getFoods().data.toFoodList() ?: emptyList()
        }
    }

}