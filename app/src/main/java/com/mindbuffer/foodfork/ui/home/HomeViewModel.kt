package com.mindbuffer.foodfork.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindbuffer.foodfork.data.DataManager
import com.mindbuffer.foodfork.data.model.db.RecipeEntity
import com.mindbuffer.foodfork.data.model.domain.Recipe
import com.mindbuffer.foodfork.data.model.others.RecipeCategories
import com.mindbuffer.foodfork.utils.AppConstants.RECIPE_PAGINATION_PAGE_SIZE
import com.mindbuffer.foodfork.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dataManager: DataManager,
) : ViewModel() {

    private val _recipesCategories = MutableStateFlow<List<RecipeCategories>>(emptyList())
    val recipesCategories: StateFlow<List<RecipeCategories>>
        get() = _recipesCategories


    private val _recipes = MutableStateFlow<NetworkResult<List<Recipe>>>(NetworkResult.Loading())
    val recipes: StateFlow<NetworkResult<List<Recipe>>>
        get() = _recipes


    private var recipesResponseList: MutableList<Recipe> = arrayListOf()
    private var pageNo = 1
    var query: String = ""
    var totalResults = -1
    var selectedChipId: Int = -1
    var categoryScrolledPosition: Int = 0

    init {
        getRecipeCategories()
        searchRecipeApiCall()
    }

    fun onSearch() {
        pageNo = 1
        recipesResponseList = arrayListOf()
        totalResults = -1
        searchRecipeApiCall()
    }

    fun searchRecipeApiCall() {
        viewModelScope.launch {
            try {
                _recipes.emit(NetworkResult.Loading())

                // just to show loading, cache is fast
                delay(1000)

                // call api
                dataManager.searchRecipeApiCall(pageNo, query).let {
                    when (it) {
                        is NetworkResult.Success -> {
                            val remoteRecipes = it.data.recipes
                            // Convert: RecipeDTOs -> RecipeCacheEntity
                            val entityRecipes: List<RecipeEntity> =
                                remoteRecipes.map { remoteRecipe -> remoteRecipe.toRecipeEntity() }
                            // insert into cache
                            dataManager.insertRecipes(entityRecipes)
                        }
                        else -> {
                            // There was a network issue
                            println("No internet connection ")
                        }
                    }
                }

                // query the cache
                val (cacheRecipesList, totalCacheItems)  = if (query.isBlank()) {
                    dataManager.getAllRecipesWithCount(
                        pageSize = RECIPE_PAGINATION_PAGE_SIZE,
                        page = pageNo
                    )
                } else {
                    dataManager.searchRecipesWithCount(
                        query = query,
                        pageSize = RECIPE_PAGINATION_PAGE_SIZE,
                        page = pageNo
                    )
                }


                pageNo++
                totalResults = totalCacheItems

                Timber.d("pageNo: $pageNo \ntotalResults: $totalCacheItems \ncacheRecipesResult $cacheRecipesList \n")

                // post List<Recipe> from cache
                val list = cacheRecipesList.map { cacheRecipe -> cacheRecipe.toRecipe() }
                if (recipesResponseList.isEmpty()) {
                    recipesResponseList.addAll(list)
                } else {
                    val oldPodcasts = recipesResponseList
                    oldPodcasts.addAll(list)
                }
                _recipes.emit(NetworkResult.Success(recipesResponseList))

            } catch (e: Exception) {
                _recipes.emit(NetworkResult.Failure(false, null, e.message.toString()))
            }
        }
    }

    private fun getRecipeCategories() {

        val categoryChips: ArrayList<RecipeCategories> = arrayListOf()

        categoryChips.add(RecipeCategories(1, "CHICKEN"))
        categoryChips.add(RecipeCategories(2, "BEEF"))
        categoryChips.add(RecipeCategories(3, "SOUP"))
        categoryChips.add(RecipeCategories(4, "DESSERT"))
        categoryChips.add(RecipeCategories(5, "VEGETARIAN"))
        categoryChips.add(RecipeCategories(6, "MILK"))
        categoryChips.add(RecipeCategories(7, "VEGAN"))
        categoryChips.add(RecipeCategories(8, "PIZZA"))
        categoryChips.add(RecipeCategories(9, "DONUT"))

        _recipesCategories.value = categoryChips
    }

    fun resetCategoryScrollPosition() {
        selectedChipId = -1
        categoryScrolledPosition = 0
    }

}