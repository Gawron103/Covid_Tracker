package com.example.covid_tracker.addcountry.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.covid_tracker.addcountry.viewmodel.AddCountryViewModel
import com.example.covid_tracker.databinding.AddCountryFragmentBinding


class AddCountryFragment : Fragment() {

    private var _binding: AddCountryFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: AddCountryViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AddCountryFragmentBinding.inflate(inflater, container, false)

        binding.btnBack.setOnClickListener {
            findNavController().navigate(AddCountryFragmentDirections.actionAddCountryFragmentToCountriesListFragment())
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AddCountryViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}