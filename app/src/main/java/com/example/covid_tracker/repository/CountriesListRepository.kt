package com.example.covid_tracker.repository

import com.example.covid_tracker.db.CountryEntry
import kotlinx.coroutines.flow.Flow

interface CountriesListRepository {

    fun getAllCountries(): Flow<List<CountryEntry>>

    suspend fun deleteCountry(countryEntry: CountryEntry)

}