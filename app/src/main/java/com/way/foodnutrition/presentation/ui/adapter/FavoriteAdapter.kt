package com.way.foodnutrition.presentation.ui.adapter

import android.view.*
import androidx.annotation.ColorInt
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
    private var multiSelection = false
    private var selectedRecipes = arrayListOf<FavoriteEntity>()
    private var favBinding = arrayListOf<ItemRecipesBinding>()

    inner class FavoriteViewHolder(private val binding: ItemRecipesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(favoriteEntity: FavoriteEntity) {
            favBinding.add(binding)

            binding.ivRecipes.load(favoriteEntity.result.image)
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
                    if (multiSelection) {
                        checkSelection(binding, favoriteEntity)
                    } else {
                        val action =
                            FavoriteRecipesFragmentDirections.actionFavoriteRecipesFragmentToDetailActivity(
                                favoriteEntity.result
                            )
                        binding.root.findNavController().navigate(action)
                    }
                }
                setOnLongClickListener {
                    if (!multiSelection) {
                        multiSelection = true
                        requireActivity.startActionMode(this@FavoriteAdapter)
                        checkSelection(binding, favoriteEntity)
                        true
                    } else {
                        checkSelection(binding, favoriteEntity)
                        false
                    }
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

    private fun checkSelection(binding: ItemRecipesBinding, currentRecipes: FavoriteEntity) {
        if (selectedRecipes.contains(currentRecipes)) {
            selectedRecipes.remove(currentRecipes)
            changeItemStyle(binding, R.color.white, R.color.lightMediumGray)
        } else {
            selectedRecipes.add(currentRecipes)
            changeItemStyle(binding, R.color.purple_500, R.color.purple_700)
        }
    }

    private fun changeItemStyle(
        binding: ItemRecipesBinding,
        @ColorInt backgroundColor: Int,
        @ColorInt strokeColor: Int
    ) {
        binding.cardViewItemRecipes.setBackgroundColor(
            ContextCompat.getColor(
                requireActivity,
                backgroundColor
            )
        )
        binding.cardViewItemRecipes.strokeColor =
            ContextCompat.getColor(requireActivity, strokeColor)
    }

    override fun onCreateActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        actionMode?.menuInflater?.inflate(R.menu.favorite_contextual_menu, menu)
        changeStatusBarColor(R.color.darker)
        return true
    }

    override fun onPrepareActionMode(p0: ActionMode?, p1: Menu?): Boolean = true
    override fun onActionItemClicked(p0: ActionMode?, p1: MenuItem?): Boolean = true
    override fun onDestroyActionMode(p0: ActionMode?) {
        favBinding.forEach { binding ->
            changeItemStyle(binding, R.color.white, R.color.lightMediumGray)
        }
        multiSelection = false
        changeStatusBarColor(R.color.purple_500)
        selectedRecipes.clear()
    }

    private fun changeStatusBarColor(@ColorInt color: Int) {
        requireActivity.window.statusBarColor = ContextCompat.getColor(requireActivity, color)
    }
}

