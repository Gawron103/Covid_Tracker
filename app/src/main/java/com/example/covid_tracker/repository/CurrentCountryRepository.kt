package com.example.covid_tracker.repository

import com.example.covid_tracker.model.CountryData
import com.example.covid_tracker.model.GeocodingModel
import retrofit2.Response

interface CurrentCountryRepository {

    suspend fun getNameForCords(lat: String, lon: String): Response<GeocodingModel>

    suspend fun getCountryCovidData(countryCode: String): Response<CountryData>

}