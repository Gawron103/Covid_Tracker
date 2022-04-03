package com.example.covid_tracker.currentlocationscreen.repository.service

import com.example.covid_tracker.currentlocationscreen.model.GeocodingModel
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodingApiService {

    @GET("geo/1.0/reverse")
    suspend fun getNameForCords(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("limit") limit: Int,
        @Query("appId") appId: String
    ): Response<GeocodingModel>

    companion object {
        private const val url = "http://api.openweathermap.org/"

        fun create(): GeocodingApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(GeocodingApiService::class.java)
        }
    }

}