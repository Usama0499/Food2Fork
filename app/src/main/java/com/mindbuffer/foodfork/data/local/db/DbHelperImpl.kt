package com.mindbuffer.foodfork.data.local.db

import com.mindbuffer.foodfork.data.model.db.RecipeEntity
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DbHelperImpl @Inject constructor(private val appDatabase: AppDatabase) : DbHelper {

    override suspend fun insertRecipe(recipe: RecipeEntity): Long {
        return appDatabase.recipeDao().insertRecipe(recipe)
    }

    override suspend fun insertRecipes(recipes: List<RecipeEntity>): LongArray {
        return appDatabase.recipeDao().insertRecipes(recipes)
    }

    override suspend fun getRecipeById(id: Int): RecipeEntity? {
        return appDatabase.recipeDao().getRecipeById(id)
    }

    override suspend fun deleteRecipes(ids: List<Int>): Int {
        return appDatabase.recipeDao().deleteRecipes(ids)
    }

    override suspend fun deleteAllRecipes() {
         appDatabase.recipeDao().deleteAllRecipes()
    }

    override suspend fun deleteRecipe(primaryKey: Int): Int {
        return appDatabase.recipeDao().deleteRecipe(primaryKey)
    }

    override suspend fun searchRecipes(
        query: String,
        page: Int,
        pageSize: Int,
    ): List<RecipeEntity> {
        return appDatabase.recipeDao().searchRecipes(query, page, pageSize)
    }

    override suspend fun countSearchResults(query: String): Int {
        return appDatabase.recipeDao().countSearchResults(query)
    }

    override suspend fun searchRecipesWithCount(
        query: String,
        page: Int,
        pageSize: Int,
    ): Pair<List<RecipeEntity>, Int> {
        val results = searchRecipes(query, page, pageSize)
        val count = countSearchResults(query)
        return Pair(results, count)
    }

    override suspend fun getAllRecipes(page: Int, pageSize: Int): List<RecipeEntity> {
        return appDatabase.recipeDao().getAllRecipes(page, pageSize)
    }

    override suspend fun countAllRecipes(): Int {
        return appDatabase.recipeDao().countAllRecipes()
    }

    override suspend fun getAllRecipesWithCount(
        page: Int,
        pageSize: Int
    ): Pair<List<RecipeEntity>, Int> {
        val results = getAllRecipes(page, pageSize)
        val count = countAllRecipes()
        return Pair(results, count)
    }
}