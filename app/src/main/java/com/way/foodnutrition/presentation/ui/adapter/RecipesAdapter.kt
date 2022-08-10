package com.way.foodnutrition.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.way.foodnutrition.R
import com.way.foodnutrition.data.remote.model.FoodRecipe
import com.way.foodnutrition.data.remote.model.Result
import com.way.foodnutrition.databinding.ItemRecipesBinding
import com.way.foodnutrition.presentation.ui.home.RecipesFragmentDirections
import org.jsoup.Jsoup
import javax.inject.Inject

class RecipesAdapter @Inject constructor() :
    RecyclerView.Adapter<RecipesAdapter.RecipesViewHolder>() {

    private var oldRecipes = emptyList<Result>()

    inner class RecipesViewHolder(private val binding: ItemRecipesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(results: Result) {
            binding.ivRecipes.load(results.image) {
                crossfade(true)
                placeholder(R.drawable.ic_error_placeholder)
                error(R.drawable.ic_error_placeholder)
            }
            binding.tvTitleRecipes.text = results.title
            binding.tvDescRecipes.text = Jsoup.parse(results.summary).text()
            binding.tvHeart.text = results.aggregateLikes.toString()
            binding.tvClock.text = results.readyInMinutes.toString()
            binding.apply {
                tvLeaf.text = results.vegan.toString()
                if (results.vegan) {
                    tvLeaf.setTextColor(ContextCompat.getColor(root.context, R.color.green))
                    icLeaf.setColorFilter(ContextCompat.getColor(root.context, R.color.green))
                } else {
                    tvLeaf.setTextColor(ContextCompat.getColor(root.context, R.color.red))
                    icLeaf.setColorFilter(ContextCompat.getColor(root.context, R.color.red))
                }
            }
            binding.root.setOnClickListener {
                val action =
                    RecipesFragmentDirections.actionRecipesFragmentToDetailActivity(results)
                binding.root.findNavController().navigate(action)
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