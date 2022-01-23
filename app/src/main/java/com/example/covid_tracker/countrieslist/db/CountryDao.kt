package com.example.covid_tracker.countrieslist.db

import androidx.room.*
import com.example.covid_tracker.countrieslist.models.CountryEntry
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface CountryDao {

    @Query("SELECT * FROM country_table")
    fun getAllCountries(): Observable<List<CountryEntry>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(countryEntry: CountryEntry): Completable

    @Delete
    fun delete(countryEntry: CountryEntry): Completable

}