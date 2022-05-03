package com.example.covid_tracker.repository

import com.example.covid_tracker.network.CountryApi
import com.example.covid_tracker.network.CovidApiResponse
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class CountryDetailsRepositoryImpl @Inject constructor(
    private val apiInterface: CountryApi
): CountryDetailsRepository  {

    override suspend fun getCountryData(name: String) =
        flow {
            val covidResponse = apiInterface.getCountryData(name)

            if (covidResponse.isSuccessful) {
                emit(CovidApiResponse.Success(covidResponse.body()))
            } else {
                val message = covidResponse.errorBody()?.string() ?: "No message"
                emit(CovidApiResponse.Error(message))
            }
        }.onStart {
            emit(CovidApiResponse.Loading(true))
        }

}