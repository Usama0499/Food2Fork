package com.mindbuffer.foodfork.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mindbuffer.foodfork.R
import com.mindbuffer.foodfork.data.model.domain.Recipe
import com.mindbuffer.foodfork.databinding.ItemRecipeBinding

class HomeAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var itemClickListener: ((item: Recipe) -> Unit)? = null


    inner class RecipeViewHolder(val binding: ItemRecipeBinding) :
        RecyclerView.ViewHolder(binding.root){
        init {
            binding.root.setOnClickListener {
                itemClickListener?.invoke(differ.currentList[adapterPosition])
            }
        }
    }


    private val differCallback = object : DiffUtil.ItemCallback<Recipe>() {
        override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){

            R.layout.item_recipe -> RecipeViewHolder(
                ItemRecipeBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> throw IllegalArgumentException("Invalid ViewType Provided")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is RecipeViewHolder -> {
                val recipe = differ.currentList[position] as Recipe
                holder.binding.apply {
                    recipeNameTv.text = recipe.title
                    recipeRatingTv.text = recipe.rating.toString()
                    Glide.with(holder.itemView.context).load(recipe.featuredImage)
                        .override(512, 512)
                        .placeholder(R.drawable.ic_food_placholder)
                        .error(R.drawable.ic_error_img)
                        .into(recipeIv)
                }
            }
        }
    }

    override fun getItemCount() = differ.currentList.size

    override fun getItemViewType(position: Int): Int {
        return when(differ.currentList[position]){
            is Recipe -> R.layout.item_recipe
            else ->{
                R.layout.item_recipe
            }
        }
    }
}