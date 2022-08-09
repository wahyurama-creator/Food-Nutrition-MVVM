package com.way.foodnutrition.presentation.ui.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class PagerAdapter(
    private val resultBundle: Bundle,
    private val fragments: ArrayList<Fragment>,
    private val title: ArrayList<String>,
    fm: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        fragments[position].arguments = resultBundle
        return fragments[position]
    }
}