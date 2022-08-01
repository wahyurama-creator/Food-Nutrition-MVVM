package com.way.foodnutrition.presentation.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.way.foodnutrition.R
import com.way.foodnutrition.databinding.FragmentRecipesBinding
import com.way.foodnutrition.presentation.ui.MainActivity
import com.way.foodnutrition.presentation.ui.adapter.RecipesAdapter
import com.way.foodnutrition.presentation.viewmodel.MainViewModel
import com.way.foodnutrition.presentation.viewmodel.RecipesViewModel
import com.way.foodnutrition.presentation.viewmodel.ViewModelFactory
import com.way.foodnutrition.utils.NetworkResult
import com.way.foodnutrition.utils.TrackLog
import com.way.foodnutrition.utils.observeOnce
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class RecipesFragment : Fragment() {

    private val args: RecipesFragmentArgs by navArgs()
    private lateinit var binding: FragmentRecipesBinding
    private lateinit var recipesAdapter: RecipesAdapter
    private lateinit var mainViewModel: MainViewModel
    private lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var recipesViewModel: RecipesViewModel

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
        viewModelFactory = (activity as MainActivity).viewModelFactory
        recipesViewModel = ViewModelProvider(this, viewModelFactory)[RecipesViewModel::class.java]
        binding.mainViewModel = mainViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setupRecyclerView()
        readFromDatabase()

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_recipesFragment_to_recipesBottomSheetFragment)
        }
    }

    private fun setupRecyclerView() {
        binding.rvRecipes.apply {
            visibility = VISIBLE
            adapter = recipesAdapter
            layoutManager = LinearLayoutManager(context)
            showShimmer(false)
        }
    }

    private fun showShimmer(isShow: Boolean) {
        with(binding.shimmerLayout) {
            visibility = VISIBLE
            if (isShow) {
                this.showShimmer(true)
            } else {
                visibility = INVISIBLE
                this.showShimmer(false)
            }
        }
    }

    private fun readFromDatabase() {
        lifecycleScope.launch(Dispatchers.Main) {
            mainViewModel.readRecipes.observeOnce(viewLifecycleOwner) { database ->
                if (database.isNotEmpty() && !args.backFromBottomShet) {
                    Log.d(RecipesFragment::class.simpleName, TrackLog.GET_DATA_FROM_DATABASE)
                    recipesAdapter.setData(database[0].foodRecipe)
                    showShimmer(false)
                    isShownRecyclerView(true)
                } else {
                    getRecipesApi()
                }
            }
        }
    }

    private fun loadOfflineData() {
        lifecycleScope.launch(Dispatchers.Main) {
            mainViewModel.readRecipes.observe(viewLifecycleOwner) { database ->
                if (database.isNotEmpty()) {
                    Log.d(RecipesFragment::class.simpleName, TrackLog.GET_DATA_FROM_DATABASE)
                    recipesAdapter.setData(database[0].foodRecipe)
                    showShimmer(false)
                }
            }
        }
    }

    private fun getRecipesApi() {
        isShownRecyclerView(false)
        Log.d(RecipesFragment::class.simpleName, TrackLog.REQUEST_GET_API)
        mainViewModel.getRecipes(recipesViewModel.setQueriesPathApi())
        mainViewModel.recipesResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    showShimmer(false)
                    isShownRecyclerView(true)
                    response.data?.let {
                        recipesAdapter.setData(it)
                    }
                }
                is NetworkResult.Error -> {
                    showShimmer(false)
                    isShownRecyclerView(true)
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    loadOfflineData()
                }
                is NetworkResult.Loading -> {
                    showShimmer(true)
                    isShownRecyclerView(false)
                }
            }
        }
    }

    private fun isShownRecyclerView(isShow: Boolean) {
        with(binding) {
            if (isShow) rvRecipes.visibility = VISIBLE
            else rvRecipes.visibility = INVISIBLE
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