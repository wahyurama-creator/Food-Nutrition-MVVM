package com.way.foodnutrition.presentation.ui.home.component

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.way.foodnutrition.databinding.FragmentRecipesBottomSheetBinding
import com.way.foodnutrition.presentation.ui.MainActivity
import com.way.foodnutrition.presentation.viewmodel.RecipesViewModel
import com.way.foodnutrition.presentation.viewmodel.RecipesViewModel.Companion.DEFAULT_DIET
import com.way.foodnutrition.presentation.viewmodel.RecipesViewModel.Companion.DEFAULT_TYPE
import com.way.foodnutrition.presentation.viewmodel.ViewModelFactory
import com.way.foodnutrition.utils.TrackLog
import java.util.*

class RecipesBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentRecipesBottomSheetBinding
    private lateinit var factory: ViewModelFactory
    private lateinit var recipesViewModel: RecipesViewModel

    private var mealTypeChip = DEFAULT_TYPE
    private var mealTypeChipId = 0
    private var dietTypeChip = DEFAULT_DIET
    private var dietTypeChipId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecipesBottomSheetBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        factory = (activity as MainActivity).viewModelFactory
        recipesViewModel = ViewModelProvider(this, factory)[RecipesViewModel::class.java]

        recipesViewModel.readMealAndDietType.asLiveData().observe(viewLifecycleOwner) {
            mealTypeChip = it.selectedMealType
            dietTypeChip = it.selectedDietType
            updateChip(it.selectedMealTypeId, binding.mealTypeChipGroup)
            updateChip(it.selectedDietTypeId, binding.dietTypeChipGroup)
        }

        setupChipGroup()
        applyChips()
    }

    private fun setupChipGroup() {
        binding.mealTypeChipGroup.setOnCheckedStateChangeListener { group, _ ->
            val chip = group.findViewById<Chip>(group.checkedChipId)
            val selectedMealType = chip.text.toString().lowercase(Locale.getDefault())
            Log.e(
                RecipesBottomSheetFragment::class.simpleName,
                "${TrackLog.SELECTED_MEAL} $selectedMealType"
            )
            mealTypeChip = selectedMealType
            mealTypeChipId = group.checkedChipId
        }
        binding.dietTypeChipGroup.setOnCheckedStateChangeListener { group, _ ->
            val chip = group.findViewById<Chip>(group.checkedChipId)
            val selectedDietType = chip.text.toString().lowercase(Locale.getDefault())
            Log.e(
                RecipesBottomSheetFragment::class.simpleName,
                "${TrackLog.SELECTED_DIET} $selectedDietType"
            )
            dietTypeChip = selectedDietType
            dietTypeChipId = group.checkedChipId
        }
    }

    private fun applyChips() {
        binding.btnBottomSheet.setOnClickListener {
            recipesViewModel.saveMealAndDietType(
                mealTypeChip,
                mealTypeChipId,
                dietTypeChip,
                dietTypeChipId
            )
            val action = RecipesBottomSheetFragmentDirections.actionRecipesBottomSheetFragmentToRecipesFragment(
                true
            )
            findNavController().navigate(action)
        }
    }

    private fun updateChip(chipId: Int, chipGroup: ChipGroup) {
        if (chipId != 0) {
            try {
                chipGroup.findViewById<Chip>(chipId).isChecked = true
            } catch (e: Exception) {
                Log.e(RecipesBottomSheetFragment::class.simpleName, e.message.toString())
            }
        }
    }
}