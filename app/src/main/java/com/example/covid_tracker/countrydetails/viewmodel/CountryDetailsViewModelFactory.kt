package com.example.covid_tracker.countrydetails.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.covid_tracker.countrydetails.repository.CountryDetailsRepository

class CountryDetailsViewModelFactory(
    private val repository: CountryDetailsRepository
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(CountryDetailsRepository::class.java)
            .newInstance(repository)
    }

}