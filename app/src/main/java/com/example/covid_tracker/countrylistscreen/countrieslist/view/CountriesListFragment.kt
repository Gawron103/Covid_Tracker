package com.example.covid_tracker.countrylistscreen.countrieslist.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.covid_tracker.R
import com.example.covid_tracker.countrylistscreen.countrieslist.adapter.CountriesListAdapter
import com.example.covid_tracker.db.CountryDatabase
import com.example.covid_tracker.countrylistscreen.countrieslist.repository.CountriesListRepository
import com.example.covid_tracker.countrylistscreen.countrieslist.viewmodel.CountriesListViewModel
import com.example.covid_tracker.countrylistscreen.countrieslist.viewmodel.CountriesListViewModelFactory
import com.example.covid_tracker.databinding.CountriesListFragmentBinding
import com.example.covid_tracker.utils.DialogCreator
import com.example.covid_tracker.utils.showSnackBar

class CountriesListFragment : Fragment() {

    private val TAG = "CountriesListFragment"

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

        countriesListViewModel.loadData()

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Fragment destroyed")
        _binding = null
    }

    private fun setupViewModel() {
        countriesListViewModel = ViewModelProvider(
            this,
            CountriesListViewModelFactory(
                CountriesListRepository(
                    CountryDatabase.getCountryDB(requireContext()).countryDao()
                )
            )
        ).get(CountriesListViewModel::class.java)
    }

    private fun setupRecyclerView() {
        countriesListAdapter = CountriesListAdapter()

        binding.rvCountriesList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = countriesListAdapter
        }

        ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                countriesListViewModel.deleteCountry(countriesListAdapter.getCountryAt(viewHolder.adapterPosition))
            }
        }).attachToRecyclerView(binding.rvCountriesList)
    }

    private fun observeCountriesData() {
        countriesListViewModel.isLoading.observe(viewLifecycleOwner, { isLoading ->
            binding.pbCountriesListLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
        })

        countriesListViewModel.countries.observe(viewLifecycleOwner, { data ->
            when (data.isEmpty()) {
                true -> { binding.tvNoCountries.visibility = View.VISIBLE }
                false -> {
                    countriesListAdapter.setCountries(data)
                    binding.tvNoCountries.visibility = View.GONE
                }
            }
        })

        countriesListViewModel.isDeletedSuccessful.observe(viewLifecycleOwner, { isDeleted ->
            when (isDeleted) {
                true -> { showSnackBar(binding.root, getString(R.string.country_list_fragment_delete_success)) }
                false -> {
                    DialogCreator(
                        R.string.dialog_title_error,
                        R.string.dialog_message_cannot_delete_country
                    ).showDialog(requireActivity())
                }
            }
        })

        countriesListViewModel.isLoadSuccessful.observe(viewLifecycleOwner, { isFetched ->
            when (isFetched) {
                true -> { showSnackBar(binding.root, getString(R.string.country_list_fragment_fetch_success)) }
                false -> {
                    DialogCreator(
                        R.string.dialog_title_error,
                        R.string.dialog_message_cannot_fetch_data
                    ).showDialog(requireActivity())
                }
            }
        })
    }

}