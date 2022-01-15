package com.example.covid_tracker.countrieslist.repository

import com.example.covid_tracker.countrieslist.db.CountryDao
import com.example.covid_tracker.countrieslist.models.Country

class CountriesListRepository(
    private val dao: CountryDao
) {

    fun getAllCountries() = dao.getAllCountries()

    fun deleteCountry(country: Country) = dao.delete(country)

}