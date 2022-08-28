package com.way.foodnutrition.presentation.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.navArgs
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.way.foodnutrition.R
import com.way.foodnutrition.data.local.model.FavoriteEntity
import com.way.foodnutrition.databinding.ActivityDetailBinding
import com.way.foodnutrition.presentation.ui.adapter.IngredientsAdapter
import com.way.foodnutrition.presentation.ui.adapter.PagerAdapter
import com.way.foodnutrition.presentation.ui.detail.ingredient.IngredientFragment
import com.way.foodnutrition.presentation.ui.detail.instruction.InstructionFragment
import com.way.foodnutrition.presentation.ui.detail.overview.OverviewFragment
import com.way.foodnutrition.presentation.viewmodel.DetailViewModel
import com.way.foodnutrition.presentation.viewmodel.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val args by navArgs<DetailActivityArgs>()

    @Inject
    lateinit var ingredientsAdapter: IngredientsAdapter

    @Inject
    lateinit var factory: ViewModelFactory
    private lateinit var detailViewModel: DetailViewModel

    private var recipesSaved = false
    private var savedRecipesId = 0
    private lateinit var menuItem: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarDetail)
        binding.toolbarDetail.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val resultBundle = Bundle()
        resultBundle.putParcelable(RECIPES_BUNDLE, args.result)
        Log.e("Bundle", args.result.toString())

        detailViewModel = ViewModelProvider(this, factory)[DetailViewModel::class.java]

        setupPagerAdapter(resultBundle)
    }

    private fun setupPagerAdapter(bundle: Bundle) {
        val fragments = ArrayList<Fragment>()
        fragments.add(OverviewFragment())
        fragments.add(IngredientFragment())
        fragments.add(InstructionFragment())

        val titles = ArrayList<String>()
        titles.add(getString(R.string.overview_fragment))
        titles.add(getString(R.string.ingredients_fragment))
        titles.add(getString(R.string.instruction_fragment))

        val pagerAdapter = PagerAdapter(
            bundle,
            fragments,
            titles,
            supportFragmentManager,
            lifecycle
        )
        binding.viewPagerDetail.adapter = pagerAdapter
        binding.viewPagerDetail.isUserInputEnabled = false
        TabLayoutMediator(
            binding.tabs,
            binding.viewPagerDetail
        ) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        menuItem = menu.findItem(R.id.saveFavoriteMenu)
        checkSavedFavorite(menuItem)
        return true
    }

    private fun checkSavedFavorite(menuItem: MenuItem) {
        detailViewModel.readFavoriteRecipes.observe(this) { favoriteEntity ->
            try {
                for (savedFavorites in favoriteEntity) {
                    if (savedFavorites.result.recipeId == args.result.recipeId) {
                        changeIconColor(menuItem, R.color.yellow)
                        savedRecipesId = savedFavorites.id
                        recipesSaved = true
                    }
                }
            } catch (e: Exception) {
                Log.d(DetailActivity::class.simpleName, e.message.toString())
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        else if (item.itemId == R.id.saveFavoriteMenu && !recipesSaved) {
            saveToFavorite(item)
        } else if (item.itemId == R.id.saveFavoriteMenu && recipesSaved) {
            deleteFromFavorite(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveToFavorite(item: MenuItem) {
        val favoriteEntity = FavoriteEntity(0, args.result)
        detailViewModel.insertFavoriteRecipes(favoriteEntity)
        changeIconColor(item, R.color.yellow)
        showSnackBar(getString(R.string.recipes_saved))
        recipesSaved = true
    }

    private fun deleteFromFavorite(item: MenuItem) {
        val favoriteEntity = FavoriteEntity(savedRecipesId, args.result)
        detailViewModel.deleteFavoriteRecipes(favoriteEntity)
        changeIconColor(item, R.color.white)
        showSnackBar(getString(R.string.recipes_removed))
        recipesSaved = false
    }

    private fun changeIconColor(item: MenuItem, @ColorInt color: Int) {
        item.icon.setTint(ContextCompat.getColor(this, color))
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        changeIconColor(menuItem, R.color.white)
    }

    companion object {
        const val RECIPES_BUNDLE = "recipes_bundle"
    }
}