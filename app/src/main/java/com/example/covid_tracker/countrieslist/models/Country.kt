package com.example.covid_tracker.countrieslist.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "country_table")
data class Country(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String
)