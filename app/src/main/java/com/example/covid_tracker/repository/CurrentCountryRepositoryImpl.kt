package com.example.covid_tracker.repository

import com.example.covid_tracker.BuildConfig
import com.example.covid_tracker.network.CountryApi
import com.example.covid_tracker.network.GeocodingApi
import javax.inject.Inject

class CurrentCountryRepositoryImpl @Inject constructor(
    private val geocodingApi: GeocodingApi,
    private val countryDataApi: CountryApi
): CurrentCountryRepository {

    override suspend fun getNameForCords(lat: String, lon: String) =
        geocodingApi
            .getNameForCords(lat, lon, 1, BuildConfig.OPEN_WEATHER_MAP_KEY)

    override suspend fun getCountryCovidData(countryCode: String) =
        countryDataApi.getCountryData(countryCode)

}