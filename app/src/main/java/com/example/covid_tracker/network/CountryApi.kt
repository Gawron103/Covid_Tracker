package com.example.covid_tracker.network

import com.example.covid_tracker.model.CountryData
import com.example.covid_tracker.utils.COVID_DATA_BASE_URL
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface CountryApi {

    @GET("v3/covid-19/countries/{country}")
    suspend fun getCountryData(@Path("country") country: String): Response<CountryData>

    // delete after migration to hilt
    companion object {
        fun create(): CountryApi {
            val retrofit = Retrofit.Builder()
                .baseUrl(COVID_DATA_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(CountryApi::class.java)
        }
    }

}
