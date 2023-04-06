package com.mindbuffer.foodfork.data

import com.mindbuffer.foodfork.data.local.db.DbHelper
import com.mindbuffer.foodfork.data.model.api.RecipeDto
import com.mindbuffer.foodfork.data.model.api.RecipeSearchResponse
import com.mindbuffer.foodfork.data.model.db.RecipeEntity
import com.mindbuffer.foodfork.data.remote.ApiHelper
import com.mindbuffer.foodfork.utils.NetworkResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataManagerImpl @Inject constructor(private val apiHelper: ApiHelper, private val dbHelper: DbHelper) :
    DataManager {

    /**
     * Remote Functions
     * */

    override suspend fun searchRecipeApiCall(page: Int, query: String): NetworkResult<RecipeSearchResponse> {
        return apiHelper.searchRecipeApiCall(page, query)
    }

    override suspend fun getRecipeApiCall(id: Int): NetworkResult<RecipeDto> {
        return apiHelper.getRecipeApiCall(id)
    }

    override suspend fun insertRecipe(recipe: RecipeEntity): Long {
       return dbHelper.insertRecipe(recipe)
    }

    override suspend fun insertRecipes(recipes: List<RecipeEntity>): LongArray {
        return dbHelper.insertRecipes(recipes)

    }

    override suspend fun getRecipeById(id: Int): RecipeEntity? {
        return dbHelper.getRecipeById(id)

    }

    override suspend fun deleteRecipes(ids: List<Int>): Int {
        return dbHelper.deleteRecipes(ids)

    }

    override suspend fun deleteAllRecipes() {
        dbHelper.deleteAllRecipes()
    }

    override suspend fun deleteRecipe(primaryKey: Int): Int {
        return dbHelper.deleteRecipe(primaryKey)
    }

    override suspend fun searchRecipes(
        query: String,
        page: Int,
        pageSize: Int,
    ): List<RecipeEntity> {
        return dbHelper.searchRecipes(query, page, pageSize)
    }

    override suspend fun countSearchResults(query: String): Int {
        return dbHelper.countSearchResults(query)
    }

    override suspend fun searchRecipesWithCount(
        query: String,
        page: Int,
        pageSize: Int,
    ): Pair<List<RecipeEntity>, Int> {
        return dbHelper.searchRecipesWithCount(query, page, pageSize)
    }

    override suspend fun getAllRecipes(page: Int, pageSize: Int): List<RecipeEntity> {
        return dbHelper.getAllRecipes(page, pageSize)
    }

    override suspend fun countAllRecipes(): Int {
        return dbHelper.countAllRecipes()
    }

    override suspend fun getAllRecipesWithCount(
        page: Int,
        pageSize: Int,
    ): Pair<List<RecipeEntity>, Int> {
        return dbHelper.getAllRecipesWithCount(page, pageSize)
    }
}