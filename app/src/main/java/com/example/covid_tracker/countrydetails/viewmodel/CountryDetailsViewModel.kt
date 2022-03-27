package com.example.covid_tracker.countrydetails.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.covid_tracker.countrydetails.model.CountryData
import com.example.covid_tracker.countrydetails.repository.CountryDetailsRepository
import kotlinx.coroutines.*

class CountryDetailsViewModel(
    private val repository: CountryDetailsRepository
) : ViewModel() {

    private val TAG = "CountryDetailsViewModel"

    private val _countryData = MutableLiveData<CountryData>()
    val countryData: LiveData<CountryData> get() = _countryData

    private val _countryDataFetchErrorOccurred = MutableLiveData<String?>()
    val countryDataFetchErrorOccurred: LiveData<String?> get() = _countryDataFetchErrorOccurred

    private val _fetchingData = MutableLiveData<Boolean>()
    val fetchingData: LiveData<Boolean> get() = _fetchingData

    private val _exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception: ${throwable.localizedMessage}")
    }

    private var _fetchJob: Job? = null

    fun fetchCountryData(name: String) {
        _fetchingData.value = true

        _fetchJob = viewModelScope.launch(Dispatchers.IO + _exceptionHandler) {
            val response = repository.getCountryData(name)

            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    _countryData.value = response.body()
                    _countryDataFetchErrorOccurred.value = null
                    _fetchingData.value = false
                }
            } else { onError(response.message()) }
        }
    }

    private fun onError(message: String) {
        _countryDataFetchErrorOccurred.value = message
        _fetchingData.value = false
    }

    override fun onCleared() {
        super.onCleared()
        _fetchJob?.cancel()
    }

}