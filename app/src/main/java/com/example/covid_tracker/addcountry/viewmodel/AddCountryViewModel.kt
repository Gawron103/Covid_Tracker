package com.example.covid_tracker.addcountry.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.covid_tracker.addcountry.repository.AddCountryRepository
import com.example.covid_tracker.db.CountryEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddCountryViewModel(
    private val repository: AddCountryRepository
) : ViewModel() {

    private val TAG = "AddCountryViewModel"

    private val _isCountrySaved = MutableLiveData<Boolean>()
    val isCountrySaved: LiveData<Boolean> get() = _isCountrySaved

    fun saveCountryInDB(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            var saveResult = false
            var logMsg = "Country NOT saved"
            val response = repository.getCountryData(name)

            if (response.isSuccessful) {
                response.body()?.let {
                    if (!checkIfCountryAlreadySaved(it.country)) {
                        saveCountry(
                            CountryEntry(
                                0,
                                it.country,
                                it.countryInfo.flag
                            )
                        )
                        saveResult = true
                        logMsg = "Country SAVED"
                    }
                }

                Log.d(TAG, logMsg)
                _isCountrySaved.postValue(saveResult)
            }
        }
    }

    private suspend fun saveCountry(countryEntry: CountryEntry) {
        repository.saveCountry(countryEntry)
    }

    private suspend fun checkIfCountryAlreadySaved(name: String): Boolean {
        return repository.isCountryAlreadySaved(name)
    }

}