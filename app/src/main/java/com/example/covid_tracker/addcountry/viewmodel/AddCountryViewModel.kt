package com.example.covid_tracker.addcountry.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.covid_tracker.addcountry.repository.AddCountryRepository
import com.example.covid_tracker.countrieslist.models.CountryEntry
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers.io

class AddCountryViewModel(
    private val repository: AddCountryRepository
) : ViewModel() {

    private val disposables = CompositeDisposable()

    private val _isCountryAdded = MutableLiveData<Boolean>()
    val isCountryAdded: LiveData<Boolean> get() = _isCountryAdded

    fun addCountry(name: String) {
        val countryExistDisposable = repository.getCountryData(name)
            .flatMapCompletable {
                when (it.isSuccessful) {
                    true -> {
                        val data = it.body()
                        repository.addCountry(CountryEntry(
                            0,
                            data!!.country,
                            data.countryInfo.flag
                        ))
                    } else -> {
                        Completable.error(RuntimeException("Cannot get country data"))
                    }
                }
            }
            .subscribeOn(io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    Log.d("AddCountryVM", "Country added to DB")
                    _isCountryAdded.postValue(true)
                },
                {
                    Log.d("AddCountryVM", "Error", it)
                    _isCountryAdded.postValue(false)
                }
            )

        disposables.add(countryExistDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
        disposables.clear()
    }

}