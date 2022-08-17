package com.way.foodnutrition.presentation.ui.joke

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

        getFoodJokes()
    }

    private fun getFoodJokes() {
        showLoading(true)
        mainViewModel.getFoodJokes()
        mainViewModel.foodJokesResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    showLoading(false)
                    binding.tvJoke.text = response.data?.text ?: "Unavailable"
                }
                is NetworkResult.Error -> {
                    showLoading(false)
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()

                    loadDataFromDatabase()
                }
                is NetworkResult.Loading -> {
                    showLoading(false)
                    showEmptyState(true)
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