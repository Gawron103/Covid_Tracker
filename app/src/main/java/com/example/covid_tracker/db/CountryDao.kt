package com.example.covid_tracker.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert
import androidx.room.Delete
import androidx.room.OnConflictStrategy

@Dao
interface CountryDao {

    @Query("SELECT * FROM country_table")
    suspend fun getAllCountries(): List<CountryEntry>

    @Query("SELECT EXISTS (SELECT 1 FROM country_table WHERE name = :name)")
    suspend fun isCountryAlreadySaved(name: String): Boolean

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(countryEntry: CountryEntry)

    @Delete
    suspend fun delete(countryEntry: CountryEntry)

}