package com.way.foodnutrition.presentation.ui.adapter

import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.way.foodnutrition.R
import com.way.foodnutrition.data.local.model.FavoriteEntity
import com.way.foodnutrition.databinding.ItemRecipesBinding
import com.way.foodnutrition.presentation.ui.favorite.FavoriteRecipesFragmentDirections
import org.jsoup.Jsoup

class FavoriteAdapter(
    private val requireActivity: FragmentActivity
) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>(), ActionMode.Callback {

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

            binding.root.apply {
                setOnClickListener {
                    val action =
                        FavoriteRecipesFragmentDirections.actionFavoriteRecipesFragmentToDetailActivity(
                            favoriteEntity.result
                        )
                    binding.root.findNavController().navigate(action)
                }
                setOnLongClickListener {
                    requireActivity.startActionMode(this@FavoriteAdapter)
                    true
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

    override fun onCreateActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        actionMode?.menuInflater?.inflate(R.menu.favorite_contextual_menu, menu)
        return true
    }

    override fun onPrepareActionMode(p0: ActionMode?, p1: Menu?): Boolean = true
    override fun onActionItemClicked(p0: ActionMode?, p1: MenuItem?): Boolean = true
    override fun onDestroyActionMode(p0: ActionMode?) {}

}