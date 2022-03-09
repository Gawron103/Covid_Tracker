package com.example.covid_tracker.countrydetails.repository.service

import com.example.covid_tracker.countrydetails.model.CountryData
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface CountryApiService {

    @GET("v3/covid-19/countries/{country}")
    suspend fun getCountryData(@Path("country") country: String): Response<CountryData>

    companion object {
        private const val url = "https://disease.sh/"

        fun create(): CountryApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(CountryApiService::class.java)
        }
    }

}