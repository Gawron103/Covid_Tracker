package com.example.covid_tracker.di

import com.example.covid_tracker.repository.AddCountryRepository
import com.example.covid_tracker.repository.AddCountryRepositoryImpl
import com.example.covid_tracker.network.CountryApi
import com.example.covid_tracker.db.CountryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object AddCountryRepositoryModule {

    @Provides
    fun provideAddCountryRepository(
        countryDao: CountryDao,
        countryApi: CountryApi
    ): AddCountryRepository {
        return AddCountryRepositoryImpl(countryDao, countryApi)
    }

}