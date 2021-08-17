package com.example.sixth.modules.maps


import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sixth.modules.cars.CarsItem
import com.example.sixth.modules.cars.CarsRepository
import com.example.sixth.util.disposeIfNotDisposed
import com.example.sixth.util.optMessage
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.Disposable
import javax.inject.Inject

@SuppressLint("CheckResult")
@HiltViewModel
class MapsViewModel @Inject constructor(
    private val carsRepository: CarsRepository
) : ViewModel() {

    private val _progress = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean>
        get() = _progress

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _markerOptionsLatLngPair = MutableLiveData<List<Pair<MarkerOptions, LatLng>>>()
    val markerOptionsPair: LiveData<List<Pair<MarkerOptions, LatLng>>>
        get() = _markerOptionsLatLngPair

    private var disposable: Disposable? = null

    init {
        request()
    }

    fun request() {
        _progress.postValue(true)
        disposable?.disposeIfNotDisposed()
        disposable =  carsRepository.getCars().map { list ->
            val latLngList = mutableListOf<Pair<MarkerOptions, LatLng>>()
            list.forEach { carsItem ->
                val latLng = LatLng(carsItem.latitude, carsItem.longitude)
                val markerOptions = MarkerOptions().position(latLng).title(carsItem.name)
                latLngList.add(markerOptions to latLng)
            }
            latLngList
        }.subscribe({
            _progress.postValue(false)
            _markerOptionsLatLngPair.postValue(it)
        }, {
            _progress.postValue(false)
            _errorMessage.postValue(it.optMessage())
        })
    }

    fun onPause() {
        disposable?.disposeIfNotDisposed()
    }

}