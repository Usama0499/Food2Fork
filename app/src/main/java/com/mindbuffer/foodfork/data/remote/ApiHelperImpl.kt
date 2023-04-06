package com.mindbuffer.foodfork.data.remote

import com.mindbuffer.foodfork.data.model.api.RecipeDto
import com.mindbuffer.foodfork.data.model.api.RecipeSearchResponse
import com.mindbuffer.foodfork.utils.NetworkResult
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(private val apiService: ApiService) : ApiHelper,
    ApiCallHandler() {

    override suspend fun searchRecipeApiCall(page: Int, query: String): NetworkResult<RecipeSearchResponse> {
        return safeApiCall { apiService.searchRecipeApiCall(page, query) }
    }

    override suspend fun getRecipeApiCall(id: Int): NetworkResult<RecipeDto> {
        return safeApiCall { apiService.getRecipeApiCall(id) }
    }

}