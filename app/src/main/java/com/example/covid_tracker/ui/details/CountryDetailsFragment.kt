package com.example.covid_tracker.ui.details

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.covid_tracker.R
import com.example.covid_tracker.databinding.CountryDetailsFragmentBinding
import com.example.covid_tracker.model.CountryData
import com.example.covid_tracker.network.CovidApiStatus
import com.example.covid_tracker.utils.DialogCreator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CountryDetailsFragment : Fragment() {

    private val TAG = "CountryDetailsFragment"

    private val args: CountryDetailsFragmentArgs by navArgs()

    private var _binding: CountryDetailsFragmentBinding? = null
    private val binding get() = _binding!!

    private val countryDetailsViewModel by viewModels<CountryDetailsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CountryDetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.ivCountryDetailsBack.setOnClickListener {
            findNavController().popBackStack()
        }

        observeCountryData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "Binding reference set to null")
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Fragment destroyed")
    }

    private fun observeCountryData() {
        countryDetailsViewModel.getCountryData(args.name).observe(viewLifecycleOwner, { result ->
            when (result.status) {
                CovidApiStatus.SUCCESS -> {
                    result.data?.let {
                        updateUI(it)
                        showErrorView(false)
                        showDataOrLoading(false)
                    }
                }
                CovidApiStatus.LOADING -> {
                    showErrorView(false)
                    showDataOrLoading(true)
                }
                CovidApiStatus.ERROR -> {
                    showErrorView(true)
                    Log.d(TAG, "Error: ${result.message}")
                    DialogCreator(
                        R.string.dialog_title_error,
                        R.string.dialog_message_cannot_fetch_data
                    ).showDialog(requireActivity())
                }
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

    private fun showDataOrLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.tvCountryDetailsName.visibility = View.GONE
            binding.ivCountryDetailsFlag.visibility = View.GONE
            binding.iCountryDetailsTotalData.tvCurrentCountryTotalLabel.visibility = View.GONE
            binding.iCountryDetailsTotalData.glTotalData.visibility = View.GONE
            binding.iCountryDetailsTodayData.tvCurrentCountryTodayLabel.visibility = View.GONE
            binding.iCountryDetailsTodayData.glTodayData.visibility = View.GONE
            binding.pbCountryDetailsLoading.visibility = View.VISIBLE
        } else {
            binding.tvCountryDetailsName.visibility = View.VISIBLE
            binding.ivCountryDetailsFlag.visibility = View.VISIBLE
            binding.iCountryDetailsTotalData.tvCurrentCountryTotalLabel.visibility = View.VISIBLE
            binding.iCountryDetailsTotalData.glTotalData.visibility = View.VISIBLE
            binding.iCountryDetailsTodayData.tvCurrentCountryTodayLabel.visibility = View.VISIBLE
            binding.iCountryDetailsTodayData.glTodayData.visibility = View.VISIBLE
            binding.pbCountryDetailsLoading.visibility = View.GONE
        }
    }

    private fun showErrorView(show: Boolean) {
        if (show) {
            binding.tvCountryDetailsErrorLabel.visibility = View.VISIBLE
            binding.tvCountryDetailsErrorMsg.visibility = View.VISIBLE
        } else {
            binding.tvCountryDetailsErrorLabel.visibility = View.GONE
            binding.tvCountryDetailsErrorMsg.visibility = View.GONE
        }
    }

}