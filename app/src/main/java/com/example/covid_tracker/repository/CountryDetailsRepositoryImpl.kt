package com.example.covid_tracker.repository

import com.example.covid_tracker.network.CountryApi
import javax.inject.Inject

class CountryDetailsRepositoryImpl @Inject constructor(
    private val apiInterface: CountryApi
): CountryDetailsRepository  {

    override suspend fun getCountryData(name: String) = apiInterface.getCountryData(name)

}