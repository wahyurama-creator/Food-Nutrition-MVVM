package com.way.foodnutrition.presentation.ui.detail.overview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.way.foodnutrition.R
import com.way.foodnutrition.databinding.FragmentOverviewBinding

class OverviewFragment : Fragment() {

    private lateinit var binding: FragmentOverviewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOverviewBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

}