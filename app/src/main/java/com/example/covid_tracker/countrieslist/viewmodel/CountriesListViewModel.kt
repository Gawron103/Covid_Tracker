package com.example.covid_tracker.countrieslist.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.covid_tracker.countrieslist.models.Country
import com.example.covid_tracker.countrieslist.repository.CountriesListRepository
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers.io

class CountriesListViewModel(
    private val repository: CountriesListRepository
) : ViewModel() {

    private val disposables = CompositeDisposable()

    private val _countries = MutableLiveData<List<Country>>()
    val countries: LiveData<List<Country>> get() = _countries

    private val _isDeleted = MutableLiveData<Boolean>()
    val isDeleted: LiveData<Boolean> get() = _isDeleted

    init {
        val getAllDisposable = repository.getAllCountries()
            .subscribeOn(io())
            .observeOn(mainThread())
            .subscribe(
                {
                    Log.d("CountriesRepo", "Data: $it")
                    _countries.postValue(it)
                },
                {
                    Log.d("CountriesRepo", "GetAll Error:", it)
                },
                {
                    Log.d("CountriesRepo", "getAllCities completed")
                }
            )

        disposables.add(getAllDisposable)
    }

    fun deleteCountry(country: Country) {
        val deleteDisposable = repository.deleteCountry(country)
            .subscribeOn(io())
            .observeOn(mainThread())
            .subscribe(
                {
                    _isDeleted.postValue(true)
                },
                {
                    Log.d("CountriesRepo", "Delete Error:", it)
                }
            )

        disposables.add(deleteDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
        disposables.clear()
    }

}