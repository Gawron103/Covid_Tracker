package com.example.covid_tracker.countrieslist.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.covid_tracker.countrieslist.adapter.CountriesListAdapter
import com.example.covid_tracker.countrieslist.db.CountryDatabase
import com.example.covid_tracker.countrieslist.repository.CountriesListRepository
import com.example.covid_tracker.countrieslist.repository.LocalDataSource
import com.example.covid_tracker.countrieslist.repository.RemoteDataSource
import com.example.covid_tracker.countrieslist.viewmodel.CountriesListViewModel
import com.example.covid_tracker.countrieslist.viewmodel.CountriesListViewModelFactory
import com.example.covid_tracker.databinding.CountriesListFragmentBinding

class CountriesListFragment : Fragment() {

    private var _binding: CountriesListFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var countriesListViewModel: CountriesListViewModel
    private lateinit var countriesListAdapter: CountriesListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CountriesListFragmentBinding.inflate(inflater, container, false)

        binding.btnAddCountry.setOnClickListener {
            findNavController().navigate(CountriesListFragmentDirections.actionCountriesListFragmentToAddCountryFragment())
        }

        setupViewModel()
        setupRecyclerView()
        observeCountriesData()

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setupViewModel() {
        countriesListViewModel = ViewModelProvider(
            requireActivity(),
            CountriesListViewModelFactory(
                CountriesListRepository(
                    LocalDataSource(CountryDatabase.getInstance(requireContext()).countryDao()),
                    RemoteDataSource()
                )
            )
        ).get(CountriesListViewModel::class.java)
    }

    private fun setupRecyclerView() {
        countriesListAdapter = CountriesListAdapter(mutableListOf())
        binding.rvCountriesList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = countriesListAdapter
        }
    }

    private fun observeCountriesData() {
        countriesListViewModel.countries.observe(viewLifecycleOwner, {
            Log.d("CitiesListViewModel", "Received data: $it")
            countriesListAdapter.updateCountries(it)
        })
    }

}