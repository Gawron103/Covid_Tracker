package com.example.covid_tracker.countrydetails.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.covid_tracker.countrydetails.model.CountryData
import com.example.covid_tracker.countrydetails.repository.CountryDetailsRepository
import com.example.covid_tracker.countrydetails.repository.service.CountryApiService
import com.example.covid_tracker.countrydetails.viewmodel.CountryDetailsViewModel
import com.example.covid_tracker.countrydetails.viewmodel.CountryDetailsViewModelFactory
import com.example.covid_tracker.databinding.CountryDetailsFragmentBinding

class CountryDetailsFragment : Fragment() {

    private val args: CountryDetailsFragmentArgs by navArgs()

    private var _binding: CountryDetailsFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var countryDetailsViewModel: CountryDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        countryDetailsViewModel = ViewModelProvider(
            requireActivity(),
            CountryDetailsViewModelFactory(
                CountryDetailsRepository(
                    CountryApiService.create()
                )
            )
        ).get(CountryDetailsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CountryDetailsFragmentBinding.inflate(inflater, container, false)

        countryDetailsViewModel.refreshData(args.name)

        binding.btnBack.setOnClickListener {
            findNavController().navigate(CountryDetailsFragmentDirections.actionCountryDetailsToCountriesListFragment())
        }

        observeData()

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun observeData() {
        countryDetailsViewModel.getDataErrorOccured.observe(viewLifecycleOwner, {
            when(it) {
                true -> { Log.d("CountryDetails", "Error occured") }
                false -> { Log.d("CountryDetails", "No error") }
            }
        })

        countryDetailsViewModel.countryData.observe(viewLifecycleOwner, {
            Log.d("CountryDetails", "Observed data: $it")
            it?.let {
                updateUI(it)
            }
        })
    }

    private fun updateUI(countryData: CountryData) {
        binding.tvCountryName.text = countryData.country
        binding.tvTodayCases.text = countryData.todayCases.toString()
        binding.tvTodayDeaths.text = countryData.todayDeaths.toString()
        binding.tvTodayRecovered.text = countryData.todayRecovered.toString()
        binding.tvTotalCases.text = countryData.cases.toString()
        binding.tvTotalDeaths.text = countryData.deaths.toString()
        binding.tvTotalRecovered.text = countryData.recovered.toString()
        binding.tvTests.text = countryData.tests.toString()
        Glide
            .with(binding.ivCountryFlag)
            .load(countryData.countryInfo.flag)
            .centerCrop()
            .into(binding.ivCountryFlag)
    }

}