package com.example.covid_tracker.currentcountry.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.covid_tracker.currentcountry.viewmodel.CurrentCountryViewModel
import com.example.covid_tracker.databinding.CurrentCountryFragmentBinding

class CurrentCountryFragment : Fragment() {

    private var _binding: CurrentCountryFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: CurrentCountryViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CurrentCountryFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CurrentCountryViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}