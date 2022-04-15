package com.example.covid_tracker.repository

import com.example.covid_tracker.model.CountryData
import com.example.covid_tracker.db.CountryEntry
import retrofit2.Response

interface AddCountryRepository {

    suspend fun getCountryData(name: String): Response<CountryData>

    suspend fun saveCountry(countryEntry: CountryEntry)

    suspend fun isCountryAlreadySaved(name: String): Boolean

}