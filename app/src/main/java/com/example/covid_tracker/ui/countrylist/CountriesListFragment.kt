package com.example.covid_tracker.ui.countrylist

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.covid_tracker.R
import com.example.covid_tracker.ui.adapter.CountriesListAdapter
import com.example.covid_tracker.databinding.CountriesListFragmentBinding
import com.example.covid_tracker.utils.DialogCreator
import com.example.covid_tracker.utils.showSnackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CountriesListFragment : Fragment() {

    private val TAG = "CountriesListFragment"

    private var _binding: CountriesListFragmentBinding? = null
    private val binding get() = _binding!!

    private val countriesListViewModel by viewModels<CountriesListViewModel>()

    private lateinit var countriesListAdapter: CountriesListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CountriesListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

        setupRecyclerView()

        observeLoading()
        observeIfCountriesLoaded()
        observeCountries()
        observeIfCountryDeleted()

        countriesListViewModel.loadData()
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

    private fun observeCountries() {
        countriesListViewModel.countries.observe(viewLifecycleOwner, { data ->
            when (data.isEmpty()) {
                true -> { binding.tvNoCountries.visibility = View.VISIBLE }
                false -> {
                    countriesListAdapter.setCountries(data)
                    binding.tvNoCountries.visibility = View.GONE
                }
            }
        })
    }

    private fun observeIfCountryDeleted() {
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
    }

    private fun observeIfCountriesLoaded() {
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

    private fun observeLoading() {
        countriesListViewModel.isLoading.observe(viewLifecycleOwner, { isLoading ->
            binding.pbCountriesListLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
        })
    }

}