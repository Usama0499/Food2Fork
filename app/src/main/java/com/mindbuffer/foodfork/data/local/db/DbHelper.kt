package com.mindbuffer.foodfork.data.local.db

import com.mindbuffer.foodfork.data.model.db.RecipeEntity
import com.mindbuffer.foodfork.utils.AppConstants


interface DbHelper {

    suspend fun insertRecipe(recipe: RecipeEntity): Long

    suspend fun insertRecipes(recipes: List<RecipeEntity>): LongArray

    suspend fun getRecipeById(id: Int): RecipeEntity?

    suspend fun deleteRecipes(ids: List<Int>): Int

    suspend fun deleteAllRecipes()

    suspend fun deleteRecipe(primaryKey: Int): Int

    suspend fun searchRecipes(
        query: String,
        page: Int,
        pageSize: Int = AppConstants.RECIPE_PAGINATION_PAGE_SIZE
    ): List<RecipeEntity>

    suspend fun countSearchResults(query: String): Int

    suspend fun searchRecipesWithCount(
        query: String,
        page: Int,
        pageSize: Int = AppConstants.RECIPE_PAGINATION_PAGE_SIZE
    ): Pair<List<RecipeEntity>, Int>

    suspend fun getAllRecipes(
        page: Int,
        pageSize: Int = AppConstants.RECIPE_PAGINATION_PAGE_SIZE
    ): List<RecipeEntity>

    suspend fun countAllRecipes(): Int

    suspend fun getAllRecipesWithCount(
        page: Int,
        pageSize: Int = AppConstants.RECIPE_PAGINATION_PAGE_SIZE
    ): Pair<List<RecipeEntity>, Int>

}