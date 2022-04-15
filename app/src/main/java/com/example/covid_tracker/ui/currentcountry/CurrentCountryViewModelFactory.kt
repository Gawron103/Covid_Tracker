package com.example.covid_tracker.ui.currentcountry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.covid_tracker.repository.CurrentCountryRepository

class CurrentCountryViewModelFactory(
    private val repository: CurrentCountryRepository
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(CurrentCountryRepository::class.java)
            .newInstance(repository)
    }

}