package com.example.covid_tracker.di

import android.content.Context
import com.example.covid_tracker.db.CountryDao
import com.example.covid_tracker.db.CountryDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context): CountryDatabase {
        return CountryDatabase.getCountryDB(appContext)
    }

    @Singleton
    @Provides
    fun provideCountryDao(db: CountryDatabase): CountryDao {
        return db.countryDao()
    }

}