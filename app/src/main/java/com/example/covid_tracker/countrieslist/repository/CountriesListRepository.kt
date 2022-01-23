package com.example.covid_tracker.countrieslist.repository

import com.example.covid_tracker.countrieslist.db.CountryDao
import com.example.covid_tracker.countrieslist.models.CountryEntry

class CountriesListRepository(
    private val dao: CountryDao
) {

    fun getAllCountries() = dao.getAllCountries()

    fun deleteCountry(countryEntry: CountryEntry) = dao.delete(countryEntry)

}