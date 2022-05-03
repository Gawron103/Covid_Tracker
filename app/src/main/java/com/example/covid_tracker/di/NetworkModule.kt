package com.example.covid_tracker.di

import com.example.covid_tracker.network.CountryApi
import com.example.covid_tracker.utils.COVID_DATA_BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    @Named("CovidData")
    fun provideCovidDataRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(COVID_DATA_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideCountryApi(@Named("CovidData") retrofit: Retrofit): CountryApi {
        return retrofit.create(CountryApi::class.java)
    }

}