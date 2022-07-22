package com.way.foodnutrition.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.way.foodnutrition.R
import com.way.foodnutrition.data.model.FoodRecipe
import com.way.foodnutrition.data.model.Result
import com.way.foodnutrition.databinding.ItemRecipesBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipesAdapter @Inject constructor() :
    RecyclerView.Adapter<RecipesAdapter.RecipesViewHolder>() {

    private var oldRecipes = emptyList<Result>()

    inner class RecipesViewHolder(private val binding: ItemRecipesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(results: Result) {
            binding.ivRecipes.load(results.image)
            binding.tvTitleRecipes.text = results.title
            binding.tvDescRecipes.text = results.summary
            binding.tvHeart.text = results.aggregateLikes.toString()
            binding.tvClock.text = results.readyInMinutes.toString()
            binding.apply {
                if (results.vegan) {
                    tvLeaf.setTextColor(ContextCompat.getColor(root.context, R.color.green))
                    icLeaf.setColorFilter(ContextCompat.getColor(root.context, R.color.green))
                } else {
                    tvLeaf.setTextColor(ContextCompat.getColor(root.context, R.color.red))
                    icLeaf.setColorFilter(ContextCompat.getColor(root.context, R.color.red))
                }
            }
        }
    }

    override fun getItemCount(): Int = oldRecipes.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipesViewHolder {
        val binding = ItemRecipesBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RecipesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipesViewHolder, position: Int) {
        holder.bind(oldRecipes[position])
    }

    fun setData(newRecipes: FoodRecipe) {
        val diffUtil = RecipesDiffUtil(oldRecipes, newRecipes.results)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        oldRecipes = newRecipes.results
        diffResults.dispatchUpdatesTo(this)
    }

}