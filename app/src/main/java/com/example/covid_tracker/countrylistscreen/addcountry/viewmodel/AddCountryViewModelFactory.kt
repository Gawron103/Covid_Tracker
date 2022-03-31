package com.example.covid_tracker.countrylistscreen.addcountry.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.covid_tracker.countrylistscreen.addcountry.repository.AddCountryRepository

class AddCountryViewModelFactory(
    private val repository: AddCountryRepository
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(AddCountryRepository::class.java)
            .newInstance(repository)
    }

}