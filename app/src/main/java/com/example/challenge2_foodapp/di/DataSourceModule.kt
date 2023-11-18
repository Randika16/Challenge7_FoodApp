package com.example.challenge2_foodapp.di

import com.example.challenge2_foodapp.core.data.source.local.datasource.CartDataSource
import com.example.challenge2_foodapp.core.data.source.local.datasource.CartDatabaseDataSource
import com.example.challenge2_foodapp.core.data.source.local.room.CartDao
import com.example.challenge2_foodapp.core.data.source.remote.datasource.RemoteAPIDataSource
import com.example.challenge2_foodapp.core.data.source.remote.datasource.RemoteDataSource
import com.example.challenge2_foodapp.core.data.source.remote.network.APIService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Singleton
    @Provides
    fun provideCartDataSource(cartDao: CartDao): CartDataSource {
        return CartDatabaseDataSource(cartDao)
    }

    @Singleton
    @Provides
    fun provideRemoteDataSource(service: APIService): RemoteDataSource {
        return RemoteAPIDataSource(service)
    }

}