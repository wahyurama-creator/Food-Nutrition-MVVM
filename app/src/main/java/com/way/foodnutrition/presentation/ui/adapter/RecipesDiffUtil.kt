package com.way.foodnutrition.presentation.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.way.foodnutrition.data.remote.model.Result

class RecipesDiffUtil(
    private val oldList: List<Result>,
    private val newList: List<Result>,
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}