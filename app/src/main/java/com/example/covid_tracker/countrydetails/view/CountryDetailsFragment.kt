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

    private val TAG = "CountryDetailsFragment"

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

        countryDetailsViewModel.fetchCountryData(args.name)

        binding.ivCountryDetailsBack.setOnClickListener {
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
        countryDetailsViewModel.countryDataFetchErrorOccurred.observe(viewLifecycleOwner, {
            val msg = it?.let {
                "Error fetching data: $it"
            } ?: "Data fetched"

            Log.d(TAG, msg)
        })

        countryDetailsViewModel.countryData.observe(viewLifecycleOwner, {
            Log.d("CountryDetails", "Observed data: $it")
            it?.let {
                updateUI(it)
            }
        })

        countryDetailsViewModel.fetchingData.observe(viewLifecycleOwner, {
            when(it) {
                true -> hideDataShowLoading()
                false -> hideLoadingShowData()
            }
        })
    }

    private fun updateUI(countryData: CountryData) {
        binding.tvCountryDetailsName.text = countryData.country

        binding.iCountryDetailsTotalData.tvCurrentCountryTotalRecovered.text = countryData.recovered.toString()
        binding.iCountryDetailsTotalData.tvCurrentCountryTotalDeaths.text = countryData.deaths.toString()
        binding.iCountryDetailsTotalData.tvCurrentCountryTotalCases.text = countryData.cases.toString()
        binding.iCountryDetailsTotalData.tvCurrentCountryTotalTests.text = countryData.tests.toString()

        binding.iCountryDetailsTodayData.tvCurrentCountryTodayRecovered.text = countryData.todayRecovered.toString()
        binding.iCountryDetailsTodayData.tvCurrentCountryTodayDeaths.text = countryData.todayDeaths.toString()
        binding.iCountryDetailsTodayData.tvCurrentCountryTodayNewCases.text = countryData.todayCases.toString()

        Glide
            .with(binding.ivCountryDetailsFlag)
            .load(countryData.countryInfo.flag)
            .centerCrop()
            .into(binding.ivCountryDetailsFlag)
    }

    private fun hideDataShowLoading() {
        binding.tvCountryDetailsName.visibility = View.GONE
        binding.ivCountryDetailsFlag.visibility = View.GONE
        binding.iCountryDetailsTotalData.tvCurrentCountryTotalLabel.visibility = View.GONE
        binding.iCountryDetailsTotalData.glTotalData.visibility = View.GONE
        binding.iCountryDetailsTodayData.tvCurrentCountryTodayLabel.visibility = View.GONE
        binding.iCountryDetailsTodayData.glTodayData.visibility = View.GONE
        binding.pbCountryDetailsLoading.visibility = View.VISIBLE
    }

    private fun hideLoadingShowData() {
        binding.tvCountryDetailsName.visibility = View.VISIBLE
        binding.ivCountryDetailsFlag.visibility = View.VISIBLE
        binding.iCountryDetailsTotalData.tvCurrentCountryTotalLabel.visibility = View.VISIBLE
        binding.iCountryDetailsTotalData.glTotalData.visibility = View.VISIBLE
        binding.iCountryDetailsTodayData.tvCurrentCountryTodayLabel.visibility = View.VISIBLE
        binding.iCountryDetailsTodayData.glTodayData.visibility = View.VISIBLE
        binding.pbCountryDetailsLoading.visibility = View.GONE
    }

}