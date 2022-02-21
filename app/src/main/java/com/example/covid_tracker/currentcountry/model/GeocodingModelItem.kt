package com.example.covid_tracker.currentcountry.model

data class GeocodingModelItem(
    val country: String,
    val lat: Double,
    val local_names: LocalNames,
    val lon: Double,
    val name: String
)