package com.way.foodnutrition.presentation.ui.joke

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.way.foodnutrition.R
import com.way.foodnutrition.databinding.FragmentFoodJokeBinding
import com.way.foodnutrition.presentation.ui.MainActivity
import com.way.foodnutrition.presentation.viewmodel.MainViewModel
import com.way.foodnutrition.presentation.viewmodel.ViewModelFactory
import com.way.foodnutrition.utils.NetworkResult
import kotlinx.coroutines.launch
import kotlin.random.Random

class FoodJokeFragment : Fragment() {

    private lateinit var binding: FragmentFoodJokeBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var factory: ViewModelFactory

    private lateinit var foodJokeText: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFoodJokeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        factory = (activity as MainActivity).viewModelFactory
        mainViewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        setupMenu()

        getFoodJokes()
    }

    private fun getFoodJokes() {
        showLoading(true)
        showEmptyState(true)
        mainViewModel.getFoodJokes()
        mainViewModel.foodJokesResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    showLoading(false)
                    showEmptyState(false)
                    if (response.data != null) {
                        binding.tvJoke.text = response.data.text
                        foodJokeText = response.data.text
                    }
                }
                is NetworkResult.Error -> {
                    showLoading(false)
                    showEmptyState(true)
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()

                    loadDataFromDatabase()
                }
                is NetworkResult.Loading -> {
                    showLoading(true)
                    Log.e(FoodJokeFragment::class.simpleName, "Loading")
                }
            }
        }
    }

    private fun loadDataFromDatabase() = lifecycleScope.launch {
        mainViewModel.readFoodJoke.observe(viewLifecycleOwner) { database ->
            if (!database.isNullOrEmpty()) {
                val randomPosition = Random(database.size).toString().toInt()
                Log.e(FoodJokeFragment::class.simpleName, randomPosition.toString())
                binding.tvJoke.text = database[randomPosition].foodJoke.text
            }
        }
    }

    private fun setupMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.food_joke_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.shareFoodJokeMenu -> {
                        val shareIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, foodJokeText)
                            type = "text/plain"
                        }
                        startActivity(shareIntent)
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun showEmptyState(isShow: Boolean) {
        binding.apply {
            lottieAnimationView.visibility = if (isShow) VISIBLE else INVISIBLE
            tvTitle.visibility = if (isShow) VISIBLE else INVISIBLE
            cardViewJoke.visibility = if (isShow) INVISIBLE else VISIBLE
        }
    }

    private fun showLoading(isShow: Boolean) {
        binding.progressBarJoke.visibility = if (isShow) VISIBLE else INVISIBLE
        binding.cardViewJoke.visibility = if (isShow) INVISIBLE else VISIBLE
    }

}