package com.example.covid_tracker.countrieslist.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.covid_tracker.db.CountryEntry
import com.example.covid_tracker.countrieslist.repository.CountriesListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CountriesListViewModel(
    private val repository: CountriesListRepository
) : ViewModel() {

    private val TAG = "CountriesListViewModel"

    private val _countries = MutableLiveData<List<CountryEntry>>()
    val countries: LiveData<List<CountryEntry>> get() = _countries

    private val _isDeleted = MutableLiveData<Boolean>()
    val isDeleted: LiveData<Boolean> get() = _isDeleted

    init {
        fetchSavedCountries()
    }

    private fun fetchSavedCountries() =
        viewModelScope.launch(Dispatchers.IO) {
            val countries = repository.getAllCountries()
            Log.d(TAG, "Countries fetched from DB")
            _countries.postValue(countries)
        }

    fun deleteCountry(countryEntry: CountryEntry) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteCountry(countryEntry)
            Log.d(TAG, "Country (${countryEntry.name}) deleted")
            _isDeleted.postValue(true)
        }

}