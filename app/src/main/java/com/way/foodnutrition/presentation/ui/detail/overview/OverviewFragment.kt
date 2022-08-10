package com.way.foodnutrition.presentation.ui.detail.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import coil.load
import com.way.foodnutrition.R
import com.way.foodnutrition.data.remote.model.Result
import com.way.foodnutrition.databinding.FragmentOverviewBinding
import com.way.foodnutrition.presentation.ui.detail.DetailActivity.Companion.RECIPES_BUNDLE

class OverviewFragment : Fragment() {

    private lateinit var binding: FragmentOverviewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOverviewBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val argsBundle = arguments?.getParcelable<Result>(RECIPES_BUNDLE) as Result
        applyDataFromBundle(argsBundle)
    }

    private fun applyDataFromBundle(data: Result) {
        binding.apply {
            ivOverview.load(data.image)
            tvTitleRecipes.text = data.title
            tvLike.text = data.aggregateLikes.toString()
            tvTime.text = data.readyInMinutes.toString()
            tvSummary.text = data.summary
            setColorOnRecipesType(data)
        }
    }

    private fun setColorOnRecipesType(data: Result) {
        binding.apply {
            when {
                data.vegetarian -> {
                    tvVegetarian.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.green
                        )
                    )
                    icCheckVegetarian.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.green
                        )
                    )
                }
                data.vegan -> {
                    tvVegan.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
                    icCheckVegan.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.green
                        )
                    )
                }
                data.glutenFree -> {
                    tvGlutenFree.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.green
                        )
                    )
                    icCheckGlutenFree.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.green
                        )
                    )
                }
                data.dairyFree -> {
                    tvDairyFree.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.green
                        )
                    )
                    icCheckDairyFree.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.green
                        )
                    )
                }
                data.veryHealthy -> {
                    tvHealthy.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
                    icCheckHealthy.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.green
                        )
                    )
                }
                data.cheap -> {
                    tvCheap.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
                    icCheckCheap.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.green
                        )
                    )
                }
            }
        }
    }
}