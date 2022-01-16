package com.example.covid_tracker.countrieslist.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.covid_tracker.countrieslist.models.CountryEntry

@Database(entities = [CountryEntry::class], version = 2)
abstract class CountryDatabase: RoomDatabase() {

    abstract fun countryDao(): CountryDao

    companion object {
        private var instance: CountryDatabase? = null

        fun getInstance(context: Context): CountryDatabase {
            if(null == instance) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    CountryDatabase::class.java,
                    "country_database"
                ).fallbackToDestructiveMigration().build()
            }

            return instance!!
        }
    }

}