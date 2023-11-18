package com.example.challenge2_foodapp.di

import android.content.Context
import com.example.challenge2_foodapp.core.utils.AssetWrapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UtilsModule {

    @Singleton
    @Provides
    fun provideAssetWrapper(
        @ApplicationContext context: Context
    ): AssetWrapper {
        return AssetWrapper(context)
    }

}