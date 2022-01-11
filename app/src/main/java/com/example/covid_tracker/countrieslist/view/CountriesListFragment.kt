package com.example.covid_tracker.countrieslist.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.covid_tracker.countrieslist.viewmodel.CountriesListViewModel
import com.example.covid_tracker.databinding.CountriesListFragmentBinding

class CountriesListFragment : Fragment() {

    private var _binding: CountriesListFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: CountriesListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CountriesListFragmentBinding.inflate(inflater, container, false)

        binding.btnAddCountry.setOnClickListener {
            findNavController().navigate(CountriesListFragmentDirections.actionCountriesListFragmentToAddCountryFragment())
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CountriesListViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}