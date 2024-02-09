package com.mindbuffer.foodfork.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.clearFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.mindbuffer.foodfork.R
import com.mindbuffer.foodfork.data.model.domain.Recipe
import com.mindbuffer.foodfork.databinding.FragmentRecipeDetailsBinding
import com.mindbuffer.foodfork.utils.NetworkResult
import com.mindbuffer.foodfork.utils.dismissLoader
import com.mindbuffer.foodfork.utils.handleApiError
import com.mindbuffer.foodfork.utils.showLoader
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipeDetailsFragment : Fragment() {

    private val binding by viewBinding<FragmentRecipeDetailsBinding>()
    private val viewModel by viewModels<RecipeDetailsViewModel>()
    private val args by navArgs<RecipeDetailsFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipe_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupObserver()
    }

    private fun setupUI() {
        // Only call the ViewModel function if the recipe ID has changed
        if (viewModel.currentRecipeId != args.recipeId) {
            viewModel.currentRecipeId = args.recipeId
            viewModel.getRecipe(args.recipeId)
        }

    }

    private fun setupObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.recipe.collectLatest {
                    dismissLoader()
                    when (it) {
                        is NetworkResult.Success -> renderRecipe(it.data)
                        is NetworkResult.Loading -> showLoader()
                        is NetworkResult.Failure -> handleApiError(it)
                    }
                }
            }
        }
    }

    private fun renderRecipe(recipe: Recipe) {
        binding.apply {
            recipeNameTv.text = recipe.title
            recipeRatingTv.text = recipe.rating.toString()
            Glide.with(requireContext()).load(recipe.featuredImage)
                .override(512, 512)
                .placeholder(R.drawable.ic_food_placholder)
                .error(R.drawable.ic_error_img)
                .into(recipeIv)
            recipeUpdatedTimeTv.text = getString(R.string.last_updated_on, recipe.dateUpdated, recipe.publisher)

            // set ingredients list in textview
            val builder = StringBuilder()
            for (ingredient in recipe.ingredients) {
                builder.append(ingredient)
                builder.append("\n")
            }
            recipeIngredientsTv.text = builder.toString()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        clearFragmentResultListener("backShareResult")
    }

}