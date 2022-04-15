package com.example.covid_tracker.repository

import com.example.covid_tracker.db.CountryDao
import com.example.covid_tracker.db.CountryEntry
import javax.inject.Inject

class CountriesListRepositoryImpl @Inject constructor(
    private val dao: CountryDao
): CountriesListRepository {

    override fun getAllCountries() = dao.getAllCountries()

    override suspend fun deleteCountry(countryEntry: CountryEntry) = dao.delete(countryEntry)

}