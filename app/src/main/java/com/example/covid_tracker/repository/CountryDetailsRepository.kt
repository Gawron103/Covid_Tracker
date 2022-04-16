package com.example.covid_tracker.repository

import com.example.covid_tracker.model.CountryData
import retrofit2.Response

interface CountryDetailsRepository {

    suspend fun getCountryData(name: String): Response<CountryData>

}