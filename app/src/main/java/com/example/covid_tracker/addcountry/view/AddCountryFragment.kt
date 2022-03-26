package com.example.covid_tracker.addcountry.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.covid_tracker.addcountry.repository.AddCountryRepository
import com.example.covid_tracker.addcountry.repository.service.CountryApiService
import com.example.covid_tracker.addcountry.viewmodel.AddCountryViewModel
import com.example.covid_tracker.addcountry.viewmodel.AddCountryViewModelFactory
import com.example.covid_tracker.db.CountryDatabase
import com.example.covid_tracker.databinding.AddCountryFragmentBinding


class AddCountryFragment : Fragment() {

    private var _binding: AddCountryFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var addCountryViewModel: AddCountryViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AddCountryFragmentBinding.inflate(inflater, container, false)

        binding.ivAddCountryBack.setOnClickListener {
            findNavController().navigate(AddCountryFragmentDirections.actionAddCountryFragmentToCountriesListFragment())
        }

        binding.btnAdd.setOnClickListener {
            if (binding.etAddCountryCountryInput.text.isNotEmpty()) {
                addCountryViewModel.saveCountryInDB(binding.etAddCountryCountryInput.text.trim().toString())
                findNavController().navigate(AddCountryFragmentDirections.actionAddCountryFragmentToCountriesListFragment())
            }
        }

        setupViewModel()
        observeData()

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setupViewModel() {
        addCountryViewModel = ViewModelProvider(
            requireActivity(),
            AddCountryViewModelFactory(
                AddCountryRepository(
                    CountryDatabase.getCountryDB(requireContext()).countryDao(),
                    CountryApiService.create()
                )
            )
        ).get(AddCountryViewModel::class.java)
    }

    private fun observeData() {
        addCountryViewModel.isCountrySaved.observe(viewLifecycleOwner, {
            val msg = if (it) "Country added" else "Cannot add Country"
            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
        })
    }

}