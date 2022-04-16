package com.example.covid_tracker.countrylistscreen.countrydetails.repository

import com.example.covid_tracker.countrylistscreen.countrydetails.repository.service.CountryApiService

class CountryDetailsRepository(
    private val apiInterface: CountryApiService
) {

    suspend fun getCountryData(name: String) = apiInterface.getCountryData(name)

}