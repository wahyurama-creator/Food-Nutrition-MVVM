package com.way.foodnutrition.presentation.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.way.foodnutrition.BuildConfig
import com.way.foodnutrition.databinding.FragmentRecipesBinding
import com.way.foodnutrition.presentation.ui.MainActivity
import com.way.foodnutrition.presentation.ui.adapter.RecipesAdapter
import com.way.foodnutrition.presentation.viewmodel.MainViewModel
import com.way.foodnutrition.utils.NetworkResult

class RecipesFragment : Fragment() {

    private lateinit var binding: FragmentRecipesBinding
    private lateinit var recipesAdapter: RecipesAdapter
    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecipesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recipesAdapter = (activity as MainActivity).recipesAdapter
        mainViewModel = (activity as MainActivity).mainViewModel

        setupRecyclerView()
        getRecipesApi()
    }

    private fun setupRecyclerView() {
        binding.rvRecipes.apply {
            visibility = View.VISIBLE
            adapter = recipesAdapter
            layoutManager = LinearLayoutManager(context)
            showShimmer(false)
        }
    }

    private fun showShimmer(isShow: Boolean) {
        with(binding.shimmerLayout) {
            visibility = View.VISIBLE
            if (isShow) {
                this.showShimmer(true)
            } else {
                visibility = View.INVISIBLE
                this.showShimmer(false)
            }
        }
    }

    private fun setQueriesPathApi(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[NUMBER] = "50"
        queries[APIKEY] = BuildConfig.apiKey
        queries[TYPE] = "snack"
        queries[DIET] = "vegan"
        queries[ADD_RECIPE_INFO] = "true"
        queries[FILL_INGREDIENTS] = "true"
        return queries
    }

    private fun getRecipesApi() {
        mainViewModel.getRecipes(setQueriesPathApi())
        mainViewModel.recipesResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    showShimmer(false)
                    response.data?.let {
                        recipesAdapter.setData(it)
                    }
                }
                is NetworkResult.Error -> {
                    showShimmer(false)
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading -> {
                    showShimmer(true)
                }
            }
        }
    }

    companion object {
        const val NUMBER = "number"
        const val APIKEY = "apiKey"
        const val TYPE = "type"
        const val DIET = "diet"
        const val ADD_RECIPE_INFO = "addRecipeInformation"
        const val FILL_INGREDIENTS = "fillIngredients"
    }
}