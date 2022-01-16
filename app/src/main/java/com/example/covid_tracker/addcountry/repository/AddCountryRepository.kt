package com.example.covid_tracker.addcountry.repository

import com.example.covid_tracker.addcountry.repository.service.CountryApiService
import com.example.covid_tracker.countrieslist.db.CountryDao
import com.example.covid_tracker.countrieslist.models.CountryEntry

class AddCountryRepository(
    private val dao: CountryDao,
    private val apiInterface: CountryApiService
) {

    fun getCountryData(name: String) = apiInterface.getCountryData(name)

    fun addCountry(countryEntry: CountryEntry) = dao.insert(countryEntry)

}