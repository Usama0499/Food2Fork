package com.mindbuffer.foodfork.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mindbuffer.foodfork.data.local.db.dao.RecipeDao
import com.mindbuffer.foodfork.data.model.db.RecipeEntity


@Database(entities = [RecipeEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun recipeDao(): RecipeDao

}