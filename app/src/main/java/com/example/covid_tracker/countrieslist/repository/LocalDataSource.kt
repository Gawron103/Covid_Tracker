package com.example.covid_tracker.countrieslist.repository

import com.example.covid_tracker.countrieslist.db.CountryDao
import com.example.covid_tracker.countrieslist.models.Country

class LocalDataSource(
    private val dao: CountryDao
) {

    fun getAllCountries() = dao.getAllCountries()

    fun addCountry(country: Country) = dao.insert(country)

    fun removeCountry(country: Country) = dao.delete(country)

}