package com.mindbuffer.foodfork.data.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mindbuffer.foodfork.data.model.domain.Recipe
import com.mindbuffer.foodfork.utils.DateUtils

@Entity(tableName = "recipes")
data class RecipeEntity(

    // Value from API
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Int,

    // Value from API
    @ColumnInfo(name = "title")
    var title: String,

    // Value from API
    @ColumnInfo(name = "publisher")
    var publisher: String,

    // Value from API
    @ColumnInfo(name = "featured_image")
    var featuredImage: String,

    // Value from API
    @ColumnInfo(name = "rating")
    var rating: Int,

    // Value from API
    @ColumnInfo(name = "source_url")
    var sourceUrl: String,

    /**
     * Value from API
     * Comma separated list of ingredients
     * EX: "carrots, cabbage, chicken,"
     */
    @ColumnInfo(name = "ingredients")
    var ingredients: String = "",

    /**
     * Value from API
     */
    @ColumnInfo(name = "date_added")
    var dateAdded: Long,

    /**
     * Value from API
     */
    @ColumnInfo(name = "date_updated")
    var dateUpdated: Long,

    /**
     * The date this recipe was "refreshed" in the cache.
     */
    @ColumnInfo(name = "date_cached")
    var dateCached: Long,
){
    fun toRecipe(): Recipe {
        return Recipe(
            id = id,
            title = title,
            featuredImage = featuredImage,
            rating = rating,
            publisher = publisher,
            sourceUrl = sourceUrl,
            ingredients = convertIngredientsToList(ingredients),
            dateAdded = DateUtils.dateToString(DateUtils.longToDate(dateAdded)),
            dateUpdated = DateUtils.dateToString(DateUtils.longToDate(dateUpdated)),
        )
    }

    private fun convertIngredientsToList(ingredientsString: String?): List<String>{
        val list: ArrayList<String> = ArrayList()
        ingredientsString?.let {
            for(ingredient in it.split(",")){
                list.add(ingredient)
            }
        }
        return list
    }
}
