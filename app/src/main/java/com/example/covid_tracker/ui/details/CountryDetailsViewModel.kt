package com.example.covid_tracker.ui.details

import android.util.Log
import androidx.lifecycle.*
import com.example.covid_tracker.network.CovidApiResponse
import com.example.covid_tracker.repository.CountryDetailsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class CountryDetailsViewModel @Inject constructor(
    private val repository: CountryDetailsRepository
) : ViewModel() {

    private val TAG = "CountryDetailsViewModel"

    private val _data = MutableLiveData<CovidApiResponse>()
    val data: LiveData<CovidApiResponse> get() = _data

    fun getCountryData(name: String) =
        flow {
            val response = repository.getCountryData(name)

            if (response.isSuccessful) {
                emit(CovidApiResponse.Success(response.body()))
            } else {
                val errorMessage = response.errorBody()?.toString() ?: "No message"
                response.errorBody()?.close()
                emit(CovidApiResponse.Error(errorMessage))
            }
        }.onStart {
            emit(CovidApiResponse.Loading(true))
        }.catch { exception ->
            val message = exception.localizedMessage ?: "No error message"
            emit(CovidApiResponse.Error(message))
        }.asLiveData()

    override fun onCleared() {
        Log.d(TAG, "CountryDetailsViewModel onCleared")
        super.onCleared()
    }

}