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
import com.example.covid_tracker.utils.DialogCreator
import com.example.covid_tracker.utils.showSnackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CountryDetailsFragment : Fragment() {

    private val TAG = "CountryDetailsFragment"

    private val args: CountryDetailsFragmentArgs by navArgs()

    private var _binding: CountryDetailsFragmentBinding? = null
    private val binding get() = _binding!!

    private val countryDetailsViewModel by viewModels<CountryDetailsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CountryDetailsFragmentBinding.inflate(inflater, container, false)

        binding.ivCountryDetailsBack.setOnClickListener {
            findNavController().popBackStack()
        }

        countryDetailsViewModel.fetchCountryData(args.name)

        observeData()

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Fragment destroyed")
        _binding = null
    }

    private fun observeData() {
        countryDetailsViewModel.countryData.observe(viewLifecycleOwner, { data ->
            data?.let {
                updateUI(data)
            }
        })

        countryDetailsViewModel.fetchingData.observe(viewLifecycleOwner, {
            when (it) {
                true -> hideDataShowLoading()
                false -> hideLoadingShowData()
            }
        })

        countryDetailsViewModel.dataFetchSuccessful.observe(viewLifecycleOwner, {
            when(it) {
                true -> showSnackBar(binding.root, getString(R.string.country_details_fragment_data_fetch_success))
                false -> {
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