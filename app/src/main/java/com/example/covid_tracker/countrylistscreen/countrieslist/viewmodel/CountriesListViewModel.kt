package com.example.covid_tracker.countrylistscreen.countrieslist.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.covid_tracker.db.CountryEntry
import com.example.covid_tracker.countrylistscreen.countrieslist.repository.CountriesListRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class CountriesListViewModel(
    private val repository: CountriesListRepository
) : ViewModel() {

    private val TAG = "CountriesListViewModel"

    private val _countries = MutableLiveData<List<CountryEntry>>()
    val countries: LiveData<List<CountryEntry>> get() = _countries

    private val _isDeletedSuccessful = MutableLiveData<Boolean>()
    val isDeletedSuccessful: LiveData<Boolean> get() = _isDeletedSuccessful

    private val _isFetchSuccessful = MutableLiveData<Boolean>()
    val isFetchSuccessful: LiveData<Boolean> get() = _isFetchSuccessful

    private val fetchExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onFetchError(throwable.localizedMessage ?: "No message")
    }
    private val deleteExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onDeleteError(throwable.localizedMessage ?: "No message")
    }

    private var _fetchJob: Job? = null
    private var _deleteJob: Job? = null

    init {
        fetchSavedCountries()
    }

    private fun fetchSavedCountries() {
        _fetchJob = viewModelScope.launch(Dispatchers.IO + fetchExceptionHandler) {
            val countries = repository.getAllCountries()
            _countries.postValue(countries)
        }
    }

    fun deleteCountry(countryEntry: CountryEntry) {
        _deleteJob = viewModelScope.launch(Dispatchers.IO + deleteExceptionHandler) {
            repository.deleteCountry(countryEntry)
            _isDeletedSuccessful.postValue(true)
        }
    }

    private fun onFetchError(message: String) {
        Log.e(TAG, "Fetch error: $message")
        _isFetchSuccessful.postValue(false)
    }

    private fun onDeleteError(message: String) {
        Log.e(TAG, "Delete error: $message")
        _isDeletedSuccessful.postValue(false)
    }

    override fun onCleared() {
        super.onCleared()
        _deleteJob?.cancel()
        _fetchJob?.cancel()
    }

}