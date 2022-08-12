package com.way.foodnutrition.presentation.ui.detail.ingredient

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.way.foodnutrition.data.remote.model.ExtendedIngredient
import com.way.foodnutrition.data.remote.model.Result
import com.way.foodnutrition.databinding.FragmentIngredientBinding
import com.way.foodnutrition.presentation.ui.adapter.IngredientsAdapter
import com.way.foodnutrition.presentation.ui.detail.DetailActivity
import com.way.foodnutrition.presentation.ui.detail.DetailActivity.Companion.RECIPES_BUNDLE

class IngredientFragment : Fragment() {

    private lateinit var binding: FragmentIngredientBinding
    private lateinit var ingredientsAdapter: IngredientsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIngredientBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val argsBundle = arguments?.getParcelable<Result>(RECIPES_BUNDLE) as Result
        ingredientsAdapter = (activity as DetailActivity).ingredientsAdapter

        setupRecyclerView(argsBundle.extendedIngredients)
    }

    private fun setupRecyclerView(data: List<ExtendedIngredient>) {
        binding.rvIngredients.apply {
            adapter = ingredientsAdapter
            layoutManager = LinearLayoutManager(context)
            ingredientsAdapter.setData(data)
        }
    }
}