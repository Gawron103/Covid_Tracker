package com.example.covid_tracker.countrylistscreen.countrieslist.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.covid_tracker.db.CountryEntry
import com.example.covid_tracker.countrylistscreen.countrieslist.repository.CountriesListRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class CountriesListViewModel(
    private val repository: CountriesListRepository
) : ViewModel() {

    private val TAG = "CountriesListViewModel"

    private val _countries = MutableLiveData<List<CountryEntry>>()
    val countries: LiveData<List<CountryEntry>> get() = _countries

    private val _isDeletedSuccessful = MutableLiveData<Boolean>()
    val isDeletedSuccessful: LiveData<Boolean> get() = _isDeletedSuccessful

    private val _isLoadSuccessful = MutableLiveData<Boolean>()
    val isLoadSuccessful: LiveData<Boolean> get() = _isLoadSuccessful

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val loadExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onLoadError(throwable.localizedMessage ?: "No message")
    }
    private val deleteExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onDeleteError(throwable.localizedMessage ?: "No message")
    }

    private var _loadJob: Job? = null
    private var _deleteJob: Job? = null

    fun loadData() {
        _loadJob = viewModelScope.launch(Dispatchers.IO + loadExceptionHandler) {
            repository.getAllCountries()
                .onStart { _isLoading.postValue(true) }
                .collect {
                    _countries.postValue(it)
                    _isLoading.postValue(false)
                }
        }
    }

    fun deleteCountry(countryEntry: CountryEntry) {
        _deleteJob = viewModelScope.launch(Dispatchers.IO + deleteExceptionHandler) {
            repository.deleteCountry(countryEntry)
            _isDeletedSuccessful.postValue(true)
        }
    }

    private fun onLoadError(message: String) {
        Log.e(TAG, "Load error: $message")
        _isLoadSuccessful.postValue(false)
        _isLoading.postValue(false)
    }

    private fun onDeleteError(message: String) {
        Log.e(TAG, "Delete error: $message")
        _isDeletedSuccessful.postValue(false)
    }

    override fun onCleared() {
        super.onCleared()
        _deleteJob?.cancel()
        _loadJob?.cancel()
    }

}