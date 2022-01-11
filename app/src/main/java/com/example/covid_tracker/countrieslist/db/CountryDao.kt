package com.example.covid_tracker.countrieslist.db

import androidx.room.*
import com.example.covid_tracker.countrieslist.models.Country
import io.reactivex.Observable

@Dao
interface CountryDao {

    @Query("SELECT * FROM country_table")
    fun getAllCountries(): Observable<List<Country>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(country: Country)

    @Delete
    fun delete(country: Country)

}