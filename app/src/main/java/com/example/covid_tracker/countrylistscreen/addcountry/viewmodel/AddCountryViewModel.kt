package com.example.covid_tracker.countrylistscreen.addcountry.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.covid_tracker.countrylistscreen.addcountry.repository.AddCountryRepository
import com.example.covid_tracker.db.CountryEntry
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AddCountryViewModel(
    private val repository: AddCountryRepository
) : ViewModel() {

    private val TAG = "AddCountryViewModel"

    private val _isCountrySaved = MutableLiveData<Boolean>()
    val isCountrySaved: LiveData<Boolean> get() = _isCountrySaved

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onSaveError(throwable.localizedMessage ?: "No message")
    }

    private var _saveInDbJob: Job? = null

    fun saveCountryInDB(name: String) {
        _saveInDbJob = viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val response = repository.getCountryData(name)

            if (response.isSuccessful) {
                response.body()?.let {
                    if (!repository.isCountryAlreadySaved(it.country)) {
                        repository.saveCountry(CountryEntry(0, it.country, it.countryInfo.flag))
                        _isCountrySaved.postValue(true)
                    } else {
                        onSaveError(response.message())
                    }
                }
            } else {
                onSaveError(response.message())
            }
        }
    }

    private fun onSaveError(message: String) {
        Log.d(TAG, "Save error: $message")
        _isCountrySaved.postValue(false)
    }

    override fun onCleared() {
        Log.d(TAG, "AddCountryViewModel onCleared")
        super.onCleared()
        _saveInDbJob?.cancel()
    }

}