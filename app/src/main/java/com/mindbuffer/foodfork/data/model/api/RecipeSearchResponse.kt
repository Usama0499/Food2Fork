package com.mindbuffer.foodfork.data.model.api

import com.google.gson.annotations.SerializedName

data class RecipeSearchResponse(
    val count: Int,

    @SerializedName("results")
    var recipes: List<RecipeDto> = arrayListOf()
)