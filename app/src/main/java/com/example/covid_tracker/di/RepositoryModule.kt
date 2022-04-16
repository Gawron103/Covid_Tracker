package com.example.covid_tracker.di

import com.example.covid_tracker.network.CountryApi
import com.example.covid_tracker.db.CountryDao
import com.example.covid_tracker.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    fun provideAddCountryRepository(
        countryDao: CountryDao,
        countryApi: CountryApi
    ): AddCountryRepository {
        return AddCountryRepositoryImpl(countryDao, countryApi)
    }

    @Provides
    fun provideCountryListRepository(
        countryDao: CountryDao
    ): CountriesListRepository {
        return CountriesListRepositoryImpl(countryDao)
    }

    @Provides
    fun provideCountryDetailsRepository(
        countryApi: CountryApi
    ): CountryDetailsRepository {
        return CountryDetailsRepositoryImpl(countryApi)
    }

}