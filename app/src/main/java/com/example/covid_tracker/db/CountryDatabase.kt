package com.example.covid_tracker.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CountryEntry::class], version = 2)
abstract class CountryDatabase: RoomDatabase() {

    abstract fun countryDao(): CountryDao

    companion object {
        private var instance: CountryDatabase? = null
        private const val dbName: String = "country_database"

        fun getCountryDB(context: Context): CountryDatabase {
            if (null == instance) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    CountryDatabase::class.java,
                    dbName
                ).fallbackToDestructiveMigration().build()
            }

            return instance!!
        }
    }

}