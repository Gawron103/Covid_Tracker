package com.example.covid_tracker.currentlocationscreen.model

data class GeocodingModelItem(
    val country: String,
    val lat: Double,
    val local_names: LocalNames,
    val lon: Double,
    val name: String
)