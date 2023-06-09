package com.mindbuffer.foodfork.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindbuffer.foodfork.data.DataManager
import com.mindbuffer.foodfork.data.model.db.RecipeEntity
import com.mindbuffer.foodfork.data.model.domain.Recipe
import com.mindbuffer.foodfork.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeDetailsViewModel @Inject constructor(
    private val dataManager: DataManager,
) : ViewModel() {

    private val _recipe = MutableStateFlow<NetworkResult<Recipe>>(NetworkResult.Loading())
    val recipe: StateFlow<NetworkResult<Recipe>>
        get() = _recipe

    var currentRecipeId: Int = -1

    fun getRecipe(recipeId: Int) {
        viewModelScope.launch {
            try {
                _recipe.emit(NetworkResult.Loading())

                // just to show loading, cache is fast
                delay(1000)

                // get from cache
                var recipe = dataManager.getRecipeById(recipeId)?.toRecipe()

                if (recipe != null) {
                    _recipe.emit(NetworkResult.Success(recipe))
                }
                // if the recipe is null, it means it was not in the cache for some reason. So get from network.
                else {
                    dataManager.getRecipeApiCall(recipeId).let {
                        when (it) {
                            is NetworkResult.Success -> {
                                // get recipe from network
                                val remoteRecipe = it.data
                                // Convert: RecipeDTOs -> RecipeCacheEntity
                                val entityRecipe: RecipeEntity = remoteRecipe.toRecipeEntity()
                                // insert into cache
                                dataManager.insertRecipe(entityRecipe)
                                // get from cache
                                recipe = dataManager.getRecipeById(recipeId)?.toRecipe()
                                // post and finish
                                if (recipe != null) {
                                    _recipe.emit(NetworkResult.Success(recipe!!))
                                } else {
                                    throw Exception("Unable to get recipe from the cache.")
                                }
                            }

                            else -> {
                                if (it is NetworkResult.Failure) _recipe.emit(it)
                            }
                        }
                    }
                }

            } catch (e: Exception) {
                _recipe.emit(NetworkResult.Failure(false, null, e.message.toString()))
            }
        }
    }

}