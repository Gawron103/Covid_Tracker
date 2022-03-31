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
import com.example.covid_tracker.db.CountryEntry
import com.example.covid_tracker.countrylistscreen.countrieslist.repository.CountriesListRepository
import com.example.covid_tracker.countrylistscreen.countrieslist.viewmodel.CountriesListViewModel
import com.example.covid_tracker.countrylistscreen.countrieslist.viewmodel.CountriesListViewModelFactory
import com.example.covid_tracker.databinding.CountriesListFragmentBinding
import com.google.android.material.snackbar.Snackbar

class CountriesListFragment : Fragment() {

    private val TAG = "CountriesListFragment"

    private var _binding: CountriesListFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var countriesListViewModel: CountriesListViewModel

    private lateinit var countriesListAdapter: CountriesListAdapter

    private val swipeCallback: ItemTouchHelper.SimpleCallback =
        object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                countriesListAdapter.onSwipeItem(viewHolder.adapterPosition)
            }
        }


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
        countriesListAdapter = CountriesListAdapter(
            mutableListOf(),
            ::adapterDeleteCountryCallback
        )

        binding.rvCountriesList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = countriesListAdapter
        }.also {
            val itemTouchHelper = ItemTouchHelper(swipeCallback)
            itemTouchHelper.attachToRecyclerView(it)
        }
    }

    private fun observeCountriesData() {
        countriesListViewModel.countries.observe(viewLifecycleOwner, {
            Log.d("CitiesListViewModel", "Received data: $it")
            if (it.isEmpty()) {
                countriesListAdapter.updateCountries(mutableListOf())
                binding.tvNoCountries.visibility = View.VISIBLE
            } else {
                countriesListAdapter.updateCountries(it)
                binding.tvNoCountries.visibility = View.GONE
            }
        })

        countriesListViewModel.isDeletedSuccessful.observe(viewLifecycleOwner, { isDeleted ->
            val text = when (isDeleted) {
                true -> { R.string.country_list_fragment_delete_success }
                false -> { R.string.country_list_fragment_delete_error }
            }
            showSnackBar(getString(text))
        })

        countriesListViewModel.isFetchSuccessful.observe(viewLifecycleOwner, { isFetched ->
            val text = when (isFetched) {
                true -> { R.string.country_list_fragment_fetch_success }
                false -> { R.string.country_list_fragment_fetch_error }
            }
            showSnackBar(getString(text))
        })
    }

    private fun adapterDeleteCountryCallback(countryEntry: CountryEntry) {
        countriesListViewModel.deleteCountry(countryEntry)
    }

    private fun showSnackBar(message: String) {
        val snackBar = parentFragment?.view?.let { Snackbar.make(it, message, Snackbar.LENGTH_LONG) }
        snackBar?.show()
    }

}