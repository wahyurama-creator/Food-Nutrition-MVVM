package com.way.foodnutrition.presentation.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.createBitmap
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.way.foodnutrition.BuildConfig
import com.way.foodnutrition.R
import com.way.foodnutrition.data.remote.model.ExtendedIngredient
import com.way.foodnutrition.databinding.ItemIngredientBinding
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class IngredientsAdapter @Inject constructor(@ApplicationContext private val context: Context) :
    RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder>() {
    private var oldIngredients = emptyList<ExtendedIngredient>()

    inner class IngredientsViewHolder(private val binding: ItemIngredientBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(ingredients: ExtendedIngredient) {
            binding.ivIngredients.load(
                BuildConfig.baseImageUrl + ingredients.name
            ) {
                crossfade(true)
                placeholder(R.drawable.ic_error_ingredient)
                error(R.drawable.ic_error_ingredient)
                createBitmap(120, 120)
            }
            binding.tvIngredientName.text =
                ingredients.name?.replaceFirstChar { it.uppercaseChar() }
            binding.tvAmountAndUnit.text =
                context.getString(R.string.ingredients_amount, ingredients.amount, ingredients.unit)
            binding.tvConsistencyIngredients.text = ingredients.consistency
            binding.tvIngredientsOriginal.text = ingredients.original
        }
    }

    override fun getItemCount(): Int = oldIngredients.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientsViewHolder {
        val binding = ItemIngredientBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return IngredientsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IngredientsViewHolder, position: Int) {
        holder.bind(oldIngredients[position])
    }

    fun setData(newIngredients: List<ExtendedIngredient>) {
        val diffUtil = RecipesDiffUtil(oldIngredients, newIngredients)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        oldIngredients = newIngredients
        diffResults.dispatchUpdatesTo(this)
    }
}