package com.example.covid_tracker.repository

import com.example.covid_tracker.network.CountryApi
import com.example.covid_tracker.db.CountryDao
import com.example.covid_tracker.db.CountryEntry
import javax.inject.Inject

class AddCountryRepositoryImpl @Inject constructor(
    private val dao: CountryDao,
    private val apiInterface: CountryApi
): AddCountryRepository {

    override suspend fun getCountryData(name: String) = apiInterface.getCountryData(name)

    override suspend fun saveCountry(countryEntry: CountryEntry) = dao.insert(countryEntry)

    override suspend fun isCountryAlreadySaved(name: String) = dao.isCountryAlreadySaved(name)

}