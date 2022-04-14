package com.example.covid_tracker.countrylistscreen.addcountry.di

import com.example.covid_tracker.countrylistscreen.addcountry.repository.AddCountryRepository
import com.example.covid_tracker.countrylistscreen.addcountry.repository.AddCountryRepositoryImpl
import com.example.covid_tracker.countrylistscreen.addcountry.repository.api.CountryApi
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