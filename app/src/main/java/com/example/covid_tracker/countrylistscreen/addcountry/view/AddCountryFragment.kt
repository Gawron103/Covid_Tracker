package com.example.covid_tracker.countrylistscreen.addcountry.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.covid_tracker.R
import com.example.covid_tracker.countrylistscreen.addcountry.repository.AddCountryRepository
import com.example.covid_tracker.countrylistscreen.addcountry.repository.service.CountryApiService
import com.example.covid_tracker.countrylistscreen.addcountry.viewmodel.AddCountryViewModel
import com.example.covid_tracker.countrylistscreen.addcountry.viewmodel.AddCountryViewModelFactory
import com.example.covid_tracker.db.CountryDatabase
import com.example.covid_tracker.databinding.AddCountryFragmentBinding
import com.example.covid_tracker.utils.DialogCreator
import com.example.covid_tracker.utils.showSnackBar


class AddCountryFragment : Fragment() {

    private val TAG = "AddCountryFragment"

    private var _binding: AddCountryFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var addCountryViewModel: AddCountryViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AddCountryFragmentBinding.inflate(inflater, container, false)

        binding.ivAddCountryBack.setOnClickListener { navigateToCountriesList() }
        binding.btnAdd.setOnClickListener { addCountry() }

        setupViewModel()
        observeData()

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Fragment destroyed")
        _binding = null
    }

    private fun setupViewModel() {
        addCountryViewModel = ViewModelProvider(
            this,
            AddCountryViewModelFactory(
                AddCountryRepository(
                    CountryDatabase.getCountryDB(requireContext()).countryDao(),
                    CountryApiService.create()
                )
            )
        ).get(AddCountryViewModel::class.java)
    }

    private fun observeData() {
        addCountryViewModel.isCountrySaved.observe(viewLifecycleOwner, { isSaved ->
            when (isSaved) {
                true -> {
                    navigateToCountriesList()
                    showSnackBar(binding.root, getString(R.string.add_country_saved_country))
                }
                false -> {
                    hideLoadingShowLayout()
                    DialogCreator(
                        R.string.dialog_title_error,
                        R.string.dialog_message_cannot_add_country
                    ).showDialog(requireActivity())
                    binding.etAddCountryCountryInput.text.clear()
                }
            }
        })
    }

    private fun navigateToCountriesList() {
        Log.d(TAG, "popBackStack")
        findNavController().popBackStack()
    }

    private fun addCountry() {
        when (binding.etAddCountryCountryInput.text.isNotEmpty()) {
            true -> {
                hideLayoutShowLoading()
                addCountryViewModel.saveCountryInDB(binding.etAddCountryCountryInput.text.trim().toString())
            }
            else -> { showSnackBar(binding.root, "Enter country name") }
        }
    }

    private fun hideLayoutShowLoading() {
        binding.ivAddCountryAddCountryImg.visibility = View.GONE
        binding.tvAddCountryInfoLabel.visibility = View.GONE
        binding.etAddCountryCountryInput.visibility = View.GONE
        binding.btnAdd.visibility = View.GONE

        binding.pbAddCountryLoading.visibility = View.VISIBLE
        binding.tvAddCountryLoadingText.visibility = View.VISIBLE
    }

    private fun hideLoadingShowLayout() {
        binding.ivAddCountryAddCountryImg.visibility = View.VISIBLE
        binding.tvAddCountryInfoLabel.visibility = View.VISIBLE
        binding.etAddCountryCountryInput.visibility = View.VISIBLE
        binding.btnAdd.visibility = View.VISIBLE

        binding.pbAddCountryLoading.visibility = View.GONE
        binding.tvAddCountryLoadingText.visibility = View.GONE
    }

}