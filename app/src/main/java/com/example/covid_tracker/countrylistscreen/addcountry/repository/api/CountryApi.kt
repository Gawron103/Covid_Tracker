package com.example.covid_tracker.countrylistscreen.addcountry.repository.api

import com.example.covid_tracker.countrylistscreen.addcountry.model.CountryData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CountryApi {

    @GET("v3/covid-19/countries/{country}")
    suspend fun getCountryData(@Path("country") country: String): Response<CountryData>

}
