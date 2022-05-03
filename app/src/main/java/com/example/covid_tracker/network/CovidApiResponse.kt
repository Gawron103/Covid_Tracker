package com.example.covid_tracker.network

import com.example.covid_tracker.model.CountryData

enum class CovidApiStatus {
    SUCCESS,
    ERROR,
    LOADING
}

sealed class CovidApiResponse(
    val status: CovidApiStatus,
    val data: CountryData?,
    val message: String?,
    val loading: Boolean?
) {
    data class Success(val _data: CountryData?): CovidApiResponse(
        status = CovidApiStatus.SUCCESS,
        data = _data,
        message = null,
        loading = null
    )

    data class Error(val _message: String): CovidApiResponse(
        status = CovidApiStatus.ERROR,
        data = null,
        message = _message,
        loading = null
    )

    data class Loading(val _loading: Boolean): CovidApiResponse(
        status = CovidApiStatus.LOADING,
        data = null,
        message = null,
        loading = _loading
    )
}
