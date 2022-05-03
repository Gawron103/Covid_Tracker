package com.example.covid_tracker.ui.currentcountry

import android.util.Log
import androidx.lifecycle.*
import com.example.covid_tracker.network.CovidApiResponse
import com.example.covid_tracker.repository.CurrentCountryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
class CurrentCountryViewModel @Inject constructor(
    private val repository: CurrentCountryRepository
) : ViewModel() {

    private val TAG = "CurrentCountryViewModel"

    private val _data = MutableLiveData<CovidApiResponse>()
    val data: LiveData<CovidApiResponse> get() = _data

    fun requestCovidData(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getCountryData(name)
                .collect {
                    _data.postValue(it)
                }
        }
    }

    override fun onCleared() {
        Log.d(TAG, "CurrentCountryViewModel onCleared")
        super.onCleared()
    }

}