package com.example.covid_tracker.currentcountry.repository

import com.example.covid_tracker.BuildConfig
import com.example.covid_tracker.currentcountry.repository.service.GeocodingApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers.io

class CurrentCountryRepository(
    private val geocodingApi: GeocodingApiService
) {

    fun getNameForCoords(lat: String, lon: String) =
        geocodingApi
            .getNameForCoords(lat, lon, 1, BuildConfig.OPEN_WEATHER_MAP_KEY)

}