package com.example.covid_tracker.ui.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.covid_tracker.R
import com.example.covid_tracker.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(R.style.Theme_Covid_Tracker)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.countriesListFragment, R.id.addCountryFragment -> {
                    binding.bottomNavigationView.visibility = View.VISIBLE
                    binding.bottomNavigationView.menu.findItem(R.id.addCountryFragment).isVisible = true
                }
                R.id.countryDetailsFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                    binding.bottomNavigationView.menu.findItem(R.id.addCountryFragment).isVisible = false
                }
                else -> {
                    binding.bottomNavigationView.visibility = View.VISIBLE
                    binding.bottomNavigationView.menu.findItem(R.id.addCountryFragment).isVisible = false
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }



}