package com.way.foodnutrition.presentation.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.way.foodnutrition.databinding.FragmentFavoriteRecipesBinding
import com.way.foodnutrition.presentation.ui.MainActivity
import com.way.foodnutrition.presentation.ui.adapter.FavoriteAdapter
import com.way.foodnutrition.presentation.viewmodel.DetailViewModel
import com.way.foodnutrition.presentation.viewmodel.ViewModelFactory

class FavoriteRecipesFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteRecipesBinding
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var factory: ViewModelFactory
    private val favoriteAdapter: FavoriteAdapter by lazy { FavoriteAdapter(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteRecipesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        factory = (activity as MainActivity).viewModelFactory
        detailViewModel = ViewModelProvider(this, factory)[DetailViewModel::class.java]

        setupRecyclerView()

        detailViewModel.readFavoriteRecipes.observe(viewLifecycleOwner) { favoriteEntity ->
            if (favoriteEntity.isNullOrEmpty()) {
                showEmptyView(true)
            } else {
                showEmptyView(false)
                favoriteAdapter.setData(favoriteEntity)
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvFavorite.apply {
            adapter = favoriteAdapter
            layoutManager = LinearLayoutManager(requireActivity())
        }
    }

    private fun showEmptyView(isShow: Boolean) {
        with(binding) {
            rvFavorite.visibility = if (isShow) INVISIBLE else VISIBLE
            lottieAnimationView.visibility = if (isShow) VISIBLE else INVISIBLE
            tvTitle.visibility = if (isShow) VISIBLE else INVISIBLE
        }
    }
}