package com.example.covid_tracker.repository

import com.example.covid_tracker.BuildConfig
import com.example.covid_tracker.network.CountryApi
import com.example.covid_tracker.network.GeocodingApiService

class CurrentCountryRepository(
    private val geocodingApi: GeocodingApiService,
    private val countryDataApi: CountryApi
) {

    suspend fun getNameForCords(lat: String, lon: String) =
        geocodingApi
            .getNameForCords(lat, lon, 1, BuildConfig.OPEN_WEATHER_MAP_KEY)

    suspend fun getCountryCovidData(countryCode: String) =
        countryDataApi.getCountryData(countryCode)

}