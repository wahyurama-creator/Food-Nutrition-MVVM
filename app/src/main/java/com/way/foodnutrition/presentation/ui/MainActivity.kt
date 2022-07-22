package com.way.foodnutrition.presentation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.way.foodnutrition.R
import com.way.foodnutrition.databinding.ActivityMainBinding
import com.way.foodnutrition.presentation.ui.adapter.RecipesAdapter
import com.way.foodnutrition.presentation.viewmodel.RecipesViewModel
import com.way.foodnutrition.presentation.viewmodel.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var recipesAdapter: RecipesAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var recipesViewModel: RecipesViewModel
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recipesViewModel = ViewModelProvider(this, viewModelFactory)[RecipesViewModel::class.java]

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerViewMain) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

}