package com.mindbuffer.foodfork.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.mindbuffer.foodfork.R
import com.mindbuffer.foodfork.data.model.domain.Recipe
import com.mindbuffer.foodfork.data.model.others.RecipeCategories
import com.mindbuffer.foodfork.databinding.FragmentHomeBinding
import com.mindbuffer.foodfork.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val binding by viewBinding<FragmentHomeBinding>()
    private val viewModel by viewModels<HomeViewModel>()
    private val homeRecyclerViewAdapter = HomeAdapter()

    private var isScrolling = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupObserver()
    }

    private fun setupUI() {
        setKeyboardActionClick()
        setRecipesRecyclerView()
    }

    private fun setupObserver() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.recipesCategories.collectLatest {
                    renderCategoriesList(it)
                }
            }
        }


        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.recipes.collectLatest {
                    dismissLoader()
                    when (it) {
                        is NetworkResult.Success -> renderList(it.data)
                        is NetworkResult.Loading -> showLoader()
                        is NetworkResult.Failure -> handleApiError(it)
                    }
                }
            }
        }
    }

    private fun renderList(data: List<Recipe>) {
        try {
            Timber.d(data.toString())
            homeRecyclerViewAdapter.differ.submitList(data.toList())
        } catch (e: Exception) {
            Timber.d(e.message)
        }

    }

    private fun setKeyboardActionClick() {
        binding.apply {
            searchEt.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // Do something when the "Done" button is clicked on the keyboard
                    viewModel.resetCategoryScrollPosition()
                    resetCategoryChipAndScrollPosition()
                    viewModel.query = searchEt.text.toString().trim()
                    viewModel.onSearch()
                    true
                } else false
            }
        }
    }

    private fun setRecipesRecyclerView() {
        binding.apply {
            recyclerView.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(requireContext())
                adapter = homeRecyclerViewAdapter
                addOnScrollListener(object :
                    PaginationScrollListener(layoutManager as LinearLayoutManager) {
                    override fun loadMoreItems() {
                        isScrolling = false
                        viewModel.searchRecipeApiCall()
                    }

                    override fun setScrolling(flag: Boolean) {
                        isScrolling = flag
                    }

                    override fun getTotalResults(): Int {
                        return viewModel.totalResults
                    }

                    override fun isScrolling(): Boolean {
                        return isScrolling
                    }

                })
            }

            // recyclerView Item Click
            homeRecyclerViewAdapter.itemClickListener = { recipe ->
                gotoRecipeDetailsFragment(recipe.id)
            }
        }
    }

    private fun gotoRecipeDetailsFragment(recipeId: Int) {
        val directions = HomeFragmentDirections.actionHomeFragmentToRecipeDetailsFragment(recipeId)
        findNavController().navigate(directions)
    }

    private fun renderCategoriesList(data: List<RecipeCategories>) {
        try {
            binding.apply {
                Timber.d(data.toString())
                for (singleRecipeCategory in data) {
                    val chip = Chip(requireActivity())
                    chip.id = singleRecipeCategory.id
                    chip.text = singleRecipeCategory.name
                    chip.isCheckable = true
                    binding.categoryChipGroup.addView(chip)
                }

                @Suppress("DEPRECATION")
                categoryChipGroup.setOnCheckedChangeListener { _, checkedId ->
                    if (checkedId != viewModel.selectedChipId) {
                        val selectedChip: Chip = categoryChipGroup.findViewById(checkedId)
                        viewModel.categoryScrolledPosition =
                            selectedChip.left - (binding.horizontalChipsView.width - selectedChip.width) / 2
                        //save selected chip position:
                        viewModel.selectedChipId = checkedId
                        //set text of selected chip in searchView
                        searchEt.setText(selectedChip.text.toString())
                        viewModel.query = searchEt.text.toString().trim()
                        viewModel.onSearch()
                    }
                }

                resetCategoryChipAndScrollPosition()
            }


        } catch (e: Exception) {
            Timber.d(e.message)
        }

    }

    private fun resetCategoryChipAndScrollPosition() {
        binding.apply {
            horizontalChipsView.post {
                if (viewModel.selectedChipId != -1) categoryChipGroup.check(viewModel.selectedChipId)
                else categoryChipGroup.clearCheck()
                horizontalChipsView.smoothScrollTo(viewModel.categoryScrolledPosition, 0)
            }
        }
    }


}