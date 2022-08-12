package com.way.foodnutrition.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.way.foodnutrition.R
import com.way.foodnutrition.data.local.model.FavoriteEntity
import com.way.foodnutrition.databinding.ItemRecipesBinding
import org.jsoup.Jsoup
import javax.inject.Inject

class FavoriteAdapter @Inject constructor() :
    RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    private var oldFavorite = emptyList<FavoriteEntity>()

    inner class FavoriteViewHolder(private val binding: ItemRecipesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(favoriteEntity: FavoriteEntity) {
            binding.ivRecipes.load(favoriteEntity.result.image) {
                crossfade(true)
                placeholder(R.drawable.ic_error_placeholder)
                error(R.drawable.ic_error_placeholder)
            }
            binding.tvTitleRecipes.text = favoriteEntity.result.title
            binding.tvDescRecipes.text = Jsoup.parse(favoriteEntity.result.summary).text()
            binding.tvHeart.text = favoriteEntity.result.aggregateLikes.toString()
            binding.tvClock.text = favoriteEntity.result.readyInMinutes.toString()
            binding.apply {
                tvLeaf.text = favoriteEntity.result.vegan.toString()
                if (favoriteEntity.result.vegan) {
                    tvLeaf.setTextColor(ContextCompat.getColor(root.context, R.color.green))
                    icLeaf.setColorFilter(ContextCompat.getColor(root.context, R.color.green))
                } else {
                    tvLeaf.setTextColor(ContextCompat.getColor(root.context, R.color.red))
                    icLeaf.setColorFilter(ContextCompat.getColor(root.context, R.color.red))
                }
            }
        }
    }

    override fun getItemCount(): Int = oldFavorite.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemRecipesBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(oldFavorite[position])
    }

    fun setData(favoriteEntity: List<FavoriteEntity>) {
        val diffUtil = RecipesDiffUtil(oldFavorite, favoriteEntity)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        oldFavorite = favoriteEntity
        diffResults.dispatchUpdatesTo(this)
    }

}