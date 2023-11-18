package com.example.challenge2_foodapp.core.data.source.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.challenge2_foodapp.core.data.source.local.entity.CartEntity
import com.example.challenge2_foodapp.core.data.source.local.entity.FoodEntity
import com.example.challenge2_foodapp.core.data.source.local.entity.FoodEntityConverter

@Database(
    entities = [FoodEntity::class, CartEntity::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(FoodEntityConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun CartDao(): CartDao

    companion object {
        private const val DB_NAME = "Food.db"
        fun getInstance(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                DB_NAME
            ).fallbackToDestructiveMigration().build()
        }
    }

}