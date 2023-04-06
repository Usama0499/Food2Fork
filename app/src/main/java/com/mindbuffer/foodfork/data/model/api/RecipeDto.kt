package com.mindbuffer.foodfork.data.model.api

import com.mindbuffer.foodfork.data.model.db.RecipeEntity
import com.mindbuffer.foodfork.utils.DateUtils

data class RecipeDto(
    val featured_image: String,
    val ingredients: List<String> = emptyList(),
    val long_date_added: Long,
    val long_date_updated: Long,
    val pk: Int,
    val publisher: String,
    val rating: Int,
    val source_url: String,
    val title: String
){
    fun toRecipeEntity(): RecipeEntity {
        return RecipeEntity(
            id = pk,
            title = title,
            featuredImage = featured_image,
            rating = rating,
            publisher = publisher,
            sourceUrl = source_url,
            ingredients = convertIngredientListToString(ingredients),
            dateAdded = long_date_added,
            dateUpdated = long_date_updated,
            dateCached = DateUtils.dateToLong(DateUtils.createTimestamp())
        )
    }

    /**
     * "Carrot, potato, Chicken, ..."
     */
    private fun convertIngredientListToString(ingredients: List<String>): String {
        val ingredientsString = StringBuilder()
        for(ingredient in ingredients){
            ingredientsString.append("$ingredient,")
        }
        return ingredientsString.toString()
    }
}