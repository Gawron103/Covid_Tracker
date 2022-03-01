package com.example.covid_tracker.currentcountry.repository

import com.example.covid_tracker.BuildConfig
import com.example.covid_tracker.countrydetails.repository.service.CountryApiService
import com.example.covid_tracker.currentcountry.repository.service.GeocodingApiService

class CurrentCountryRepository(
    private val geocodingApi: GeocodingApiService,
    private val countryDataApi: CountryApiService
) {

    fun getNameForCoords(lat: String, lon: String) =
        geocodingApi
            .getNameForCoords(lat, lon, 1, BuildConfig.OPEN_WEATHER_MAP_KEY)

    fun getCountryCovidData(countryCode: String) =
        countryDataApi.getCountryData(countryCode)

}