package com.example.challenge2_foodapp.di

import com.example.challenge2_foodapp.core.data.CartRepository
import com.example.challenge2_foodapp.core.data.CartRepositoryImpl
import com.example.challenge2_foodapp.core.data.FoodRepository
import com.example.challenge2_foodapp.core.data.FoodRepositoryImpl
import com.example.challenge2_foodapp.core.data.source.local.datasource.CartDataSource
import com.example.challenge2_foodapp.core.data.source.remote.datasource.RemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideCartRepository(
        remoteDataSource: RemoteDataSource,
        localDataSource: CartDataSource
    ): CartRepository {
        return CartRepositoryImpl(remoteDataSource, localDataSource)
    }

    @Singleton
    @Provides
    fun provideFoodRepository(
        remoteDataSource: RemoteDataSource
    ): FoodRepository {
        return FoodRepositoryImpl(remoteDataSource)
    }
}
