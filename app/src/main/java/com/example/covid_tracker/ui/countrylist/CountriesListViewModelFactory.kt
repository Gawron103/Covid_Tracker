package com.example.covid_tracker.ui.countrylist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.covid_tracker.repository.CountriesListRepository

class CountriesListViewModelFactory(
    private val repository: CountriesListRepository
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(CountriesListRepository::class.java)
            .newInstance(repository)
    }

}