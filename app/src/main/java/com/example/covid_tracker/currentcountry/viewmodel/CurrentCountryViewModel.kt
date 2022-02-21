package com.example.covid_tracker.currentcountry.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.covid_tracker.currentcountry.repository.CurrentCountryRepository
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers.io
import retrofit2.http.Query

class CurrentCountryViewModel(
    private val repository: CurrentCountryRepository
) : ViewModel() {

    private val TAG = "CurrentCountryViewModel"

    private val countryName = MutableLiveData<String>()
    val countryNameLiveData: LiveData<String> get() = countryName

    private val errorGettingCountryName = MutableLiveData<Boolean>()
    val errorGettingCountryNameLiveData: LiveData<Boolean> get() = errorGettingCountryName

    private var countryNameDisposable: Disposable? = null

    fun refreshLocationData(lat: String, lon: String) {
        Log.d(TAG, "refreshLocationData triggered")
        Log.d(TAG, "params: $lat and $lon")

        countryNameDisposable = repository.getNameForCoords(lat, lon)
            .subscribeOn(io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    countryName.postValue(it[0].country)
                    errorGettingCountryName.postValue(false)
                },
                {
                    errorGettingCountryName.postValue(true)
                }
            )
    }

    override fun onCleared() {
        super.onCleared()
        countryNameDisposable?.dispose()
    }

}