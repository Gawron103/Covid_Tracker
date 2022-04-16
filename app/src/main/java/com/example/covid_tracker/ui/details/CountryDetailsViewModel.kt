package com.example.covid_tracker.ui.details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.covid_tracker.model.CountryData
import com.example.covid_tracker.repository.CountryDetailsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class CountryDetailsViewModel @Inject constructor(
    private val repository: CountryDetailsRepository
) : ViewModel() {

    private val TAG = "CountryDetailsViewModel"

    private val _countryData = MutableLiveData<CountryData>()
    val countryData: LiveData<CountryData> get() = _countryData

    private val _dataFetchSuccessful = MutableLiveData<Boolean>()
    val dataFetchSuccessful: LiveData<Boolean> get() = _dataFetchSuccessful

    private val _fetchingData = MutableLiveData<Boolean>()
    val fetchingData: LiveData<Boolean> get() = _fetchingData

    private val _exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onFetchError("Exception: ${throwable.localizedMessage}")
    }

    private var _fetchJob: Job? = null

    fun fetchCountryData(name: String) {
        _fetchingData.value = true

        _fetchJob = viewModelScope.launch(Dispatchers.IO + _exceptionHandler) {
            val response = repository.getCountryData(name)

            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    _countryData.value = response.body()
                    _dataFetchSuccessful.value = true
                }
            } else { onFetchError(response.message()) }

            _fetchingData.postValue(false)
        }
    }

    private fun onFetchError(message: String) {
        Log.d(TAG, "Fetch error: $message")
        _dataFetchSuccessful.postValue(false)
    }

    override fun onCleared() {
        Log.d(TAG, "CountryDetailsViewModel onCleared")
        super.onCleared()
        _fetchJob?.cancel()
    }

}