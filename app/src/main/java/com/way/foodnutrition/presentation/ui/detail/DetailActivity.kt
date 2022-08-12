package com.way.foodnutrition.presentation.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import com.way.foodnutrition.R
import com.way.foodnutrition.databinding.ActivityDetailBinding
import com.way.foodnutrition.presentation.ui.adapter.IngredientsAdapter
import com.way.foodnutrition.presentation.ui.adapter.PagerAdapter
import com.way.foodnutrition.presentation.ui.detail.ingredient.IngredientFragment
import com.way.foodnutrition.presentation.ui.detail.instruction.InstructionFragment
import com.way.foodnutrition.presentation.ui.detail.overview.OverviewFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val args by navArgs<DetailActivityArgs>()

    @Inject
    lateinit var ingredientsAdapter: IngredientsAdapter

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
        TabLayoutMediator(
            binding.tabs,
            binding.viewPagerDetail
        ) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val RECIPES_BUNDLE = "recipes_bundle"
    }
}