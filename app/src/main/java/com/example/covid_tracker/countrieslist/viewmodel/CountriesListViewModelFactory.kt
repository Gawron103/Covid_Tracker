package com.example.covid_tracker.countrieslist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.covid_tracker.countrieslist.repository.CountriesListRepository

class CountriesListViewModelFactory(
    private val repository: CountriesListRepository
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(CountriesListRepository::class.java)
            .newInstance(repository)
    }

}