package com.example.challenge2_foodapp.di

import android.content.Context
import com.example.challenge2_foodapp.core.data.source.remote.network.APIService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideRemoteAPIService(): APIService {
        return APIService.invoke()
    }
}
