package com.example.covid_tracker.countrydetails.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.covid_tracker.countrydetails.model.CountryData

import com.example.covid_tracker.countrydetails.repository.CountryDetailsRepository
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers.io

class CountryDetailsViewModel(
    private val repository: CountryDetailsRepository
) : ViewModel() {

    private val disposable = CompositeDisposable()

    private val _countryData = MutableLiveData<CountryData>()
    val countryData: LiveData<CountryData> get() = _countryData

    private val _getDataErrorOccured = MutableLiveData<Boolean>()
    val getDataErrorOccured: LiveData<Boolean> get() = _getDataErrorOccured

    fun refreshData(name: String) {
        val getDataDisposable = repository.getCountryData(name)
            .subscribeOn(io())
            .observeOn(mainThread())
            .subscribe(
                {
                    Log.d("CountryDetailsVM", "GetCountryData Response: $it")
                    when (it.isSuccessful) {
                        true -> {
                            _countryData.postValue(it.body())
                            _getDataErrorOccured.postValue(false)
                        }
                        false -> {
                            _getDataErrorOccured.postValue(true)
                        }
                    }
                },
                {
                    Log.d("CountryDetailsVM", "GetCountryData Error:", it)
                }
            )

        disposable.add(getDataDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
        disposable.clear()
    }

}