package com.example.covid_tracker.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "country_table")
data class CountryEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val flagUrl: String
)