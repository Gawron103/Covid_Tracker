package com.example.covid_tracker.countrieslist.repository

class CountriesListRepository(
    private val localDataSource: LocalDataSource,
    private val remoteSource: RemoteDataSource
) {

    fun getAllCities() = localDataSource.getAllCountries()

}