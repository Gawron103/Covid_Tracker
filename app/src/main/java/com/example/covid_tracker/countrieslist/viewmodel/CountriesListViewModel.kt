package com.example.covid_tracker.countrieslist.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.covid_tracker.countrieslist.models.Country
import com.example.covid_tracker.countrieslist.repository.CountriesListRepository
import io.reactivex.schedulers.Schedulers.io

class CountriesListViewModel(
    private val repository: CountriesListRepository
) : ViewModel() {

    private val _countries = MutableLiveData<List<Country>>()
    val countries: LiveData<List<Country>> get() = _countries

    init {
        repository.getAllCities()
            .subscribeOn(io())
            .subscribe(
                {
                    Log.d("CountriesRepo", "Data: $it")
                    _countries.postValue(it)
                },
                {
                    Log.d("CountriesRepo", "Error:", it)
                },
                {
                    Log.d("CountriesRepo", "getAllCities completed")
                }
            )
    }

}