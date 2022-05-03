package com.example.covid_tracker.repository

import com.example.covid_tracker.network.CovidApiResponse
import kotlinx.coroutines.flow.Flow

interface CurrentCountryRepository {

    suspend fun getCountryData(name: String): Flow<CovidApiResponse>

}