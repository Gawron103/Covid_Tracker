package com.example.covid_tracker.countrydetails.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.covid_tracker.countrydetails.viewmodel.CountryDetailsViewModel
import com.example.covid_tracker.databinding.CountryDetailsFragmentBinding

class CountryDetails : Fragment() {

    private var _binding: CountryDetailsFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: CountryDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CountryDetailsFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CountryDetailsViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}