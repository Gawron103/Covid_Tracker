package com.example.covid_tracker.addcountry.repository.service

import com.example.covid_tracker.addcountry.model.CountryData
import io.reactivex.Single
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface CountryApiService {

    @GET("v3/covid-19/countries/{country}")
    fun getCountryData(@Path("country") country: String): Single<Response<CountryData>>

    companion object {
        private const val url = "https://disease.sh/"

        fun create(): CountryApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
            return retrofit.create(CountryApiService::class.java)
        }
    }

}