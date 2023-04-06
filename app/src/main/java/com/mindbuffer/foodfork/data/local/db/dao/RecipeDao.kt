package com.mindbuffer.foodfork.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mindbuffer.foodfork.data.model.db.RecipeEntity
import com.mindbuffer.foodfork.utils.AppConstants.RECIPE_PAGINATION_PAGE_SIZE

@Dao
interface RecipeDao {

    @Insert
    suspend fun insertRecipe(recipe: RecipeEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRecipes(recipes: List<RecipeEntity>): LongArray

    @Query("SELECT * FROM recipes WHERE id = :id")
    suspend fun getRecipeById(id: Int): RecipeEntity?

    @Query("DELETE FROM recipes WHERE id IN (:ids)")
    suspend fun deleteRecipes(ids: List<Int>): Int

    @Query("DELETE FROM recipes")
    suspend fun deleteAllRecipes()

    @Query("DELETE FROM recipes WHERE id = :primaryKey")
    suspend fun deleteRecipe(primaryKey: Int): Int

    @Query(
        """
        SELECT COUNT(*) FROM recipes 
        WHERE title LIKE '%' || :query || '%'
        OR ingredients LIKE '%' || :query || '%'
        """
    )
    suspend fun countSearchResults(query: String): Int

    /**
     * Retrieve recipes for a particular page.
     * Ex: page = 2 retrieves recipes from 30-60.
     * Ex: page = 3 retrieves recipes from 60-90
     */
    @Query(
        """
        SELECT * FROM recipes 
        WHERE title LIKE '%' || :query || '%'
        OR ingredients LIKE '%' || :query || '%'  
        ORDER BY date_updated DESC LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)
        """
    )
    suspend fun searchRecipes(
        query: String,
        page: Int,
        pageSize: Int = RECIPE_PAGINATION_PAGE_SIZE,
    ): List<RecipeEntity>


    @Query(
        """
        SELECT COUNT(*) FROM recipes
        """
    )
    suspend fun countAllRecipes(): Int

    /**
     * Same as 'searchRecipes' function, but no query.
     */
    @Query(
        """
        SELECT * FROM recipes 
        ORDER BY date_updated DESC LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)
    """
    )
    suspend fun getAllRecipes(
        page: Int,
        pageSize: Int = RECIPE_PAGINATION_PAGE_SIZE,
    ): List<RecipeEntity>

}