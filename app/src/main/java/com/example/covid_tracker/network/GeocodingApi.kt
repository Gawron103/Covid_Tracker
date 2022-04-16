package com.example.covid_tracker.network

import com.example.covid_tracker.model.GeocodingModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodingApi {

    @GET("geo/1.0/reverse")
    suspend fun getNameForCords(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("limit") limit: Int,
        @Query("appId") appId: String
    ): Response<GeocodingModel>

}