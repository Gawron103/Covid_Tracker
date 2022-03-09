package com.example.covid_tracker.addcountry.repository

import com.example.covid_tracker.addcountry.repository.service.CountryApiService
import com.example.covid_tracker.db.CountryDao
import com.example.covid_tracker.db.CountryEntry

class AddCountryRepository(
    private val dao: CountryDao,
    private val apiInterface: CountryApiService
) {

    suspend fun getCountryData(name: String) = apiInterface.getCountryData(name)

    suspend fun saveCountry(countryEntry: CountryEntry) = dao.insert(countryEntry)

    suspend fun isCountryAlreadySaved(name: String) = dao.isCountryAlreadySaved(name)

}