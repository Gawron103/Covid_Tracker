package com.example.covid_tracker.repository

import com.example.covid_tracker.network.CountryApi

class CountryDetailsRepository(
    private val apiInterface: CountryApi
) {

    suspend fun getCountryData(name: String) = apiInterface.getCountryData(name)

}