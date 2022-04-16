package com.example.covid_tracker.currentlocationscreen.repository

import com.example.covid_tracker.BuildConfig
import com.example.covid_tracker.countrylistscreen.countrydetails.repository.service.CountryApiService
import com.example.covid_tracker.currentlocationscreen.repository.service.GeocodingApiService

class CurrentCountryRepository(
    private val geocodingApi: GeocodingApiService,
    private val countryDataApi: CountryApiService
) {

    suspend fun getNameForCords(lat: String, lon: String) =
        geocodingApi
            .getNameForCords(lat, lon, 1, BuildConfig.OPEN_WEATHER_MAP_KEY)

    suspend fun getCountryCovidData(countryCode: String) =
        countryDataApi.getCountryData(countryCode)

}