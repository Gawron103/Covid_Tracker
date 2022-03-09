package com.example.covid_tracker.countrieslist.repository

import com.example.covid_tracker.db.CountryDao
import com.example.covid_tracker.db.CountryEntry

class CountriesListRepository(
    private val dao: CountryDao
) {

    suspend fun getAllCountries() = dao.getAllCountries()

    suspend fun deleteCountry(countryEntry: CountryEntry) = dao.delete(countryEntry)

}