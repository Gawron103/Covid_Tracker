package com.example.covid_tracker.countrylistscreen.countrieslist.repository

import com.example.covid_tracker.db.CountryDao
import com.example.covid_tracker.db.CountryEntry

class CountriesListRepository(
    private val dao: CountryDao
) {

    fun getAllCountries() = dao.getAllCountries()

    suspend fun deleteCountry(countryEntry: CountryEntry) = dao.delete(countryEntry)

}