package com.example.covid_tracker.currentcountry.utils

import android.annotation.SuppressLint
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest

object LocationUtils {

    @SuppressLint("MissingPermission")
    fun startLocationDataUpdate(
        fusedLocationClient: FusedLocationProviderClient,
        locationCallback: LocationCallback,
        accuracyPriority: Int,
        updateInterval: Long,
    ) {
        val locationRequest = LocationRequest.create().apply {
            priority = accuracyPriority
            interval = updateInterval
            fastestInterval = 0
            numUpdates = 1
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()
        )
    }

    fun stopLocationDataUpdate(
        fusedLocationClient: FusedLocationProviderClient,
        locationCallback: LocationCallback,
    ) {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

}