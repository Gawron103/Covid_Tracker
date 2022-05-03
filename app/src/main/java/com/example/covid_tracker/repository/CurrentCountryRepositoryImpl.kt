package com.example.covid_tracker.repository

import com.example.covid_tracker.network.CountryApi
import com.example.covid_tracker.network.CovidApiResponse
import kotlinx.coroutines.flow.flow

import javax.inject.Inject

class CurrentCountryRepositoryImpl @Inject constructor(
    private val countryDataApi: CountryApi
): CurrentCountryRepository {

    override suspend fun getCountryData(name: String) =
        flow {
            emit(CovidApiResponse.Loading(true))

            val covidResponse = countryDataApi.getCountryData(name)

            if (covidResponse.isSuccessful) {
                emit(CovidApiResponse.Success(covidResponse.body()))
            } else {
                val message = covidResponse.errorBody()?.string() ?: "No message"
                emit(CovidApiResponse.Error(message))
            }
        }

}