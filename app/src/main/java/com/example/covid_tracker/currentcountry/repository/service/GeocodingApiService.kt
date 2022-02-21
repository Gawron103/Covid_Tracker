package com.example.covid_tracker.currentcountry.repository.service

import com.example.covid_tracker.currentcountry.model.GeocodingModel
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface GeocodingApiService {

    @GET("geo/1.0/reverse")
    fun getNameForCoords(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("limit") limit: Int,
        @Query("appId") appId: String
    ): Single<GeocodingModel>

    companion object {
        private const val url = "http://api.openweathermap.org/"

        fun create(): GeocodingApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
            return retrofit.create(GeocodingApiService::class.java)
        }
    }

}