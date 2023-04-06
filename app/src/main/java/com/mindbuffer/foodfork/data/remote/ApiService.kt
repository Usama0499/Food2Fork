package com.mindbuffer.foodfork.data.remote

import com.mindbuffer.foodfork.data.model.*
import com.mindbuffer.foodfork.data.model.api.RecipeDto
import com.mindbuffer.foodfork.data.model.api.RecipeSearchResponse
import retrofit2.http.*

interface ApiService {

    @GET(ApiEndPoint.ENDPOINT_SEARCH)
    suspend fun searchRecipeApiCall(
        @Query("page") page: Int,
        @Query("query") query: String
    ): RecipeSearchResponse

    @GET(ApiEndPoint.ENDPOINT_GET)
    suspend fun getRecipeApiCall(@Query("id") id: Int): RecipeDto

}
