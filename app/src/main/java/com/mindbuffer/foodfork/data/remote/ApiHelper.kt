package com.mindbuffer.foodfork.data.remote

import com.mindbuffer.foodfork.data.model.*
import com.mindbuffer.foodfork.data.model.api.RecipeDto
import com.mindbuffer.foodfork.data.model.api.RecipeSearchResponse
import com.mindbuffer.foodfork.utils.NetworkResult

interface ApiHelper {

    suspend fun searchRecipeApiCall(page: Int, query: String): NetworkResult<RecipeSearchResponse>

    suspend fun getRecipeApiCall(id: Int): NetworkResult<RecipeDto>

}