package com.example.covid_tracker.ui.currentcountry

import android.util.Log
import androidx.lifecycle.*
import com.example.covid_tracker.network.CovidApiResponse
import com.example.covid_tracker.repository.CurrentCountryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
class CurrentCountryViewModel @Inject constructor(
    private val repository: CurrentCountryRepository
) : ViewModel() {

    private val TAG = "CurrentCountryViewModel"

    private val _countryCovidData = MutableLiveData<CovidApiResponse>()
    val countryCovidData: LiveData<CovidApiResponse> get() = _countryCovidData

    private var requestCountryCovidDataJob: Job? = null

    fun requestCountryCovidData(name: String) {
        requestCountryCovidDataJob = viewModelScope.launch(Dispatchers.IO) {
            repository.getCountryData(name)
                .cancellable()
                .collect {
                    _countryCovidData.postValue(it)
                }
        }
    }

    override fun onCleared() {
        Log.d(TAG, "CurrentCountryViewModel onCleared")
        super.onCleared()
        requestCountryCovidDataJob?.cancel()
    }

}